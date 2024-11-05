package me.kpavlov.elven.ai

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiChatModelName
import me.kpavlov.langchain4j.MockChatLanguageModel

private const val TEST = true

object AiConnector {
    val model: ChatLanguageModel =
        if (TEST) {
            MockChatLanguageModel()
        } else {
            OpenAiChatModel
                .OpenAiChatModelBuilder()
                .modelName(OpenAiChatModelName.GPT_4_O_MINI)
                .temperature(0.7)
                .maxCompletionTokens(150)
                .apiKey("demo")
                .build()
        }
}
