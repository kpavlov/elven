package me.kpavlov.elven.ai

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import me.kpavlov.elven.utils.Secrets
import me.kpavlov.langchain4j.MockChatLanguageModel

private const val TEST = false

object AiConnector {
    val model: ChatLanguageModel =
        if (TEST) {
            MockChatLanguageModel()
        } else {
            val apiKey = Secrets.get("OPENAI_API_KEY")
            requireNotNull(apiKey) { "OPENAI_API_KEY must be set" }
            OpenAiChatModel
                .OpenAiChatModelBuilder()
                .modelName("gpt-4o-mini")
                .temperature(0.7)
                .maxCompletionTokens(250)
                .apiKey(apiKey)
                .logRequests(true)
                .logResponses(true)
                .strictJsonSchema(true)
                .build()
        }
}
