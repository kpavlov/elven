package me.kpavlov.elven.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.memory.ChatMemory
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.service.AiServices
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.log.logger
import me.kpavlov.elven.ai.AiConnector.model

class AiStrategy(
    name: String,
) {
    private lateinit var systemPrompt: String
    private val log = logger<AiStrategy>()
    private val embeddingStore = InMemoryEmbeddingStore<TextSegment>()
    private lateinit var assistant: Assistant
    private lateinit var chatMemory: ChatMemory

    init {
        KtxAsync.launch {
            val systemPromptFile = Gdx.files.internal("characters/$name/system-prompt.md")
            systemPrompt = systemPromptFile.readString()

            loadWorldKnowledge()
            loadKnowledge(name)

            chatMemory =
                MessageWindowChatMemory
                    .builder()
                    .id(name)
                    .maxMessages(20)
                    .build()
            assistant = createAssistant()
        }
    }

    private fun createAssistant(): Assistant =
        AiServices
            .builder(Assistant::class.java)
            .chatLanguageModel(model)
            .systemMessageProvider {
                systemPrompt
            }.chatMemory(
                chatMemory,
            ).contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
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

    fun reply(question: String): String = assistant.chat(question)
}
