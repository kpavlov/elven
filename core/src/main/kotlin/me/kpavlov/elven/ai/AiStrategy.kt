package me.kpavlov.elven.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.rag.content.Content
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.ModerationException
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.log.Logger
import me.kpavlov.elven.ai.AiConnector.model
import me.kpavlov.elven.ai.AiConnector.moderationModel
import me.kpavlov.elven.ai.AiConnector.streamingModel
import me.kpavlov.elven.characters.AiCharacter
import me.kpavlov.elven.characters.PlayerCharacter
import java.util.concurrent.Executors

private val textDocumentParser = TextDocumentParser()

class AiStrategy(
    character: AiCharacter,
) {
    private val log = Logger("AiStrategy[${character.name}]")
    private val embeddingStore = InMemoryEmbeddingStore<TextSegment>()
    private val assistant: Assistant
    private val name = character.name

    private val memory = Memory(character)

    init {
        val virtualThreadDispatcher = Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()

        KtxAsync.launch(virtualThreadDispatcher) {
            loadWorldKnowledge()
        }
        KtxAsync.launch(virtualThreadDispatcher) {
            loadKnowledge(name)
        }

        val systemPromptFile = Gdx.files.internal("characters/$name/system-prompt.md")
        val systemPrompt = systemPromptFile.readString()

        assistant = createAssistant(character, systemPrompt)
    }

    private fun createAssistant(
        character: AiCharacter,
        systemPrompt: String,
    ): Assistant {
        val builder =
            AiServices
                .builder(Assistant::class.java)
                .streamingChatLanguageModel(streamingModel)
                .moderationModel(moderationModel)
                .chatLanguageModel(model)
                .systemMessageProvider {
                    systemPrompt
                }.chatMemoryProvider(memory.chatMemoryProvider)
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))

        character.toolProvider?.let {
            log.info { "Using tool provider: ${it.javaClass.name}" }
            builder.toolProvider(it)
        }
        character.tools?.let {
            log.info { "Using tools: ${it.joinToString(", ")}" }
            builder.tools(it)
        }
        return builder.build()
    }

    private suspend fun loadWorldKnowledge() {
        val knowledgeDir = Gdx.files.internal("knowledge/")
        indexDocumentsFrom(knowledgeDir)
    }

    private suspend fun loadKnowledge(name: String) {
        val knowledgeDir = Gdx.files.internal("characters/$name/knowledge/")
        indexDocumentsFrom(knowledgeDir)
    }

    private suspend fun indexDocumentsFrom(knowledgeDir: FileHandle) {
        if (!knowledgeDir.exists() || !knowledgeDir.isDirectory) {
            return
        }

        // load documents
        val documents = FileSystemDocumentLoader.loadDocumentsRecursively(knowledgeDir.path(), textDocumentParser)
        log.info { "Loaded ${documents.size} documents: " }

        // ingest documents
        EmbeddingStoreIngestor.ingest(documents, embeddingStore)
        log.info { "Ingested ${documents.size} documents" }
    }

    @Suppress("LongParameterList")
    fun streamingReply(
        aiCharacter: AiCharacter,
        player: PlayerCharacter,
        question: String,
        onPartialResponse: (String) -> Unit = {},
        onCompleteResponse: (Reply) -> Unit = {},
        onRetrieved: (List<Content>) -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        assistant
            .streamChat(
                playerName = player.name,
                userMessage = question,
            ).onPartialResponse {
                // log.debug { "Partial: $it" }
                onPartialResponse.invoke(it)
            }.onCompleteResponse {
                // log.info { "Completed: $it" }
                onCompleteResponse.invoke(Reply(it.aiMessage().text() ?: "", coins = 0))
            }.onRetrieved { retrieved ->
                // log.info { "Retrieved: $retrieved" }
                onRetrieved.invoke(retrieved)
            }.onError {
                log.error(it) { "Error executing streaming request: $it" }
                onError.invoke(it)
            }.start()
    }

    fun reply(
        aiCharacter: AiCharacter,
        player: PlayerCharacter,
        question: String,
    ): Reply {
        try {
            val userMessage =
                if (aiCharacter.coins != null) {
                    "You have ${aiCharacter.coins} coins. Reply to: ```$question```"
                } else {
                    question
                }
            val aiReply =
                assistant.chat(
                    playerName = player.name,
                    userMessage = userMessage,
                )
            log.info { "${aiCharacter.name} AI replied with ${aiReply.coins} coins 🤑 and text:\n${aiReply.text}" }
            return aiReply
        } catch (e: ModerationException) {
            log.info(e) { "Content policy violation: $question" }
            return Reply(
                text = "Your message \"$question\" violates Content Policy. You will be punished!",
                coins = -50,
            )
        }
    }

    fun getChatHistory(withPlayer: PlayerCharacter) = memory.getChatHistory(withPlayer)
}
