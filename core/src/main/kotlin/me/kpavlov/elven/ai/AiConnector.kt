package me.kpavlov.elven.ai

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import me.kpavlov.langchain4j.MockChatLanguageModel

private const val TEST = false

object AiConnector {
    val model: ChatLanguageModel =
        if (TEST) {
            MockChatLanguageModel()
        } else {
            OpenAiChatModel
                .OpenAiChatModelBuilder()
                .modelName("gpt-4o-mini")
                .modelName("gpt-4o")
                .temperature(0.7)
                .maxCompletionTokens(150)
                .apiKey(OPENAI_API_KEY)
                .build()
        }
}
