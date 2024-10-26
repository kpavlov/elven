package me.kpavlov.elven.ai

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.chat.DisabledChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel

private const val TEST = true

object AiConnector {
    val model: ChatLanguageModel =
        if (TEST) {
            DisabledChatLanguageModel()
        } else {
            OpenAiChatModel
                .OpenAiChatModelBuilder()
                .modelName("gpt-4o-mini")
                .temperature(0.7)
                .maxTokens(100)
                .maxCompletionTokens(50)
                .apiKey("demo")
                .build()
        }
}
