package me.kpavlov.elven.ai

import com.badlogic.gdx.Gdx
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.service.AiServices
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.log.logger
import me.kpavlov.elven.ai.AiConnector.model
import kotlin.io.path.Path

private const val BASE_PATH = "../core/src/main/resources"

class AiStrategy(
    name: String,
) {
    private lateinit var systemPrompt: String
    private val log = logger<AiStrategy>()
    private val embeddingStore = InMemoryEmbeddingStore<TextSegment>()
    private lateinit var assistant: Assistant

    init {
        KtxAsync.launch {
            val systemPromptFile = Gdx.files.classpath("ai/characters/$name/system-prompt.md")
            systemPrompt = systemPromptFile.readString()

            loadKnowledge(name)

            assistant = createAssistant()
        }
    }

    private fun createAssistant(): Assistant =
        AiServices
            .builder(Assistant::class.java)
            .chatLanguageModel(model)
            .systemMessageProvider {
                systemPrompt
            }.chatMemory(MessageWindowChatMemory.withMaxMessages(20))
            .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
            .build()

    private fun loadKnowledge(name: String) {
        val knowledgeDir = Gdx.files.classpath("ai/characters/$name/knowledge/")
        if (!knowledgeDir.exists()) {
            return
        }

        // load documents
        val fsPath = Path(BASE_PATH, knowledgeDir.path())
        val fsAbsolutePath = fsPath.toAbsolutePath()
        val textDocumentParser = TextDocumentParser()
        val documents = FileSystemDocumentLoader.loadDocuments(fsAbsolutePath, textDocumentParser)
        log.info { "Loaded documents: $documents" }

        // ingest documents
        EmbeddingStoreIngestor.ingest(documents, embeddingStore)
        log.info { "Ingested ${documents.size} documents" }
    }

    fun reply(question: String): String = assistant.chat(question)
}
