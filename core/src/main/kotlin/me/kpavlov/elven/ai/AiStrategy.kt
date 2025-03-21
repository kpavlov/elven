package me.kpavlov.elven.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.TextContent
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.internal.Json
import dev.langchain4j.memory.chat.ChatMemoryProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.ModerationException
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.log.logger
import me.kpavlov.elven.ai.AiConnector.model
import me.kpavlov.elven.ai.AiConnector.moderationModel
import me.kpavlov.elven.characters.AiCharacter
import me.kpavlov.elven.characters.PlayerCharacter

private const val CHAT_MEMORY_WINDOW_SIZE = 100

class AiStrategy(
    val character: AiCharacter,
) {
    private lateinit var systemPrompt: String
    private val log = logger<AiStrategy>()
    private val embeddingStore = InMemoryEmbeddingStore<TextSegment>()
    private lateinit var assistant: Assistant
    private val name = character.name

    val chatMemoryStore = InMemoryChatMemoryStore()

    val chatMemoryProvider =
        ChatMemoryProvider { memoryId ->
            MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(CHAT_MEMORY_WINDOW_SIZE)
                .chatMemoryStore(chatMemoryStore)
                .build()
        }

    init {
        KtxAsync.launch {
            val systemPromptFile = Gdx.files.internal("characters/$name/system-prompt.md")
            systemPrompt = systemPromptFile.readString()

            loadWorldKnowledge()
            loadKnowledge(name)

            assistant = createAssistant()
        }
    }

    private fun createAssistant(): Assistant =
        AiServices
            .builder(Assistant::class.java)
            .moderationModel(moderationModel)
            .chatLanguageModel(model)
            .systemMessageProvider {
                systemPrompt
            }.chatMemoryProvider(chatMemoryProvider)
            .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
            .tools(character.tools)
            .build()

    private fun loadWorldKnowledge() {
        val knowledgeDir = Gdx.files.internal("knowledge/")
        loadDocumentsFrom(knowledgeDir)
    }

    private fun loadKnowledge(name: String) {
        val knowledgeDir = Gdx.files.internal("characters/$name/knowledge/")
        loadDocumentsFrom(knowledgeDir)
    }

    private fun loadDocumentsFrom(knowledgeDir: FileHandle) {
        if (!knowledgeDir.exists() || !knowledgeDir.isDirectory) {
            return
        }

        // load documents
        val textDocumentParser = TextDocumentParser()
        val documents = FileSystemDocumentLoader.loadDocuments(knowledgeDir.path(), textDocumentParser)
        log.info { "Loaded documents: $documents" }

        // ingest documents
        EmbeddingStoreIngestor.ingest(documents, embeddingStore)
        log.info { "Ingested ${documents.size} documents" }
    }

    fun reply(
        aiCharacter: AiCharacter,
        player: PlayerCharacter,
        question: String,
    ): Reply {
        try {
            val aiReply =
                assistant.chat(
                    playerName = player.name,
                    userMessage = question,
                    coins = aiCharacter.coins,
                )
            log.info { "LLM replied with ${aiReply.coins} coins 🤑 and text:\n${aiReply.text}" }
            return aiReply
        } catch (e: ModerationException) {
            log.info(e) { "Content policy violation: $question" }
            return Reply(
                text = "Your message \"$question\" violates Content Policy. You will be punished!",
                coins = -50,
            )
        }
    }

    fun getChatHistory(withPlayer: PlayerCharacter): List<ChatMessage> =
        chatMemoryProvider
            .get(withPlayer.name)
            .messages()
            .map {
                return@map when (it) {
                    is AiMessage -> {
                        val text = it.text()
                        if (text.isNullOrBlank()) {
                            return@map null
                        }
                        try {
                            Json.fromJson<Reply>(text, Reply::class.java)?.let {
                                return@map ChatMessage(from = character, text = it.text, coins = it.coins)
                            }
                        } catch (_: Exception) {
                            // can't deserialize
                            return@map null
                        }
                    }

                    is UserMessage -> {
                        val textContent = it.contents().firstOrNull() as TextContent?
                        textContent?.text()?.let { text ->
                            val userMessage =
                                text.substringBefore(
                                    "\n\nAnswer using the following information:\n",
                                )
                            ChatMessage(from = withPlayer, text = userMessage, coins = 0)
                        }
                    }

                    else -> {
                        null
                    }
                }
            }.filterNotNull()
}
