package me.kpavlov.elven.ai

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.moderation.ModerationModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiModerationModel
import me.kpavlov.elven.utils.Secrets
import me.kpavlov.langchain4j.MockChatLanguageModel
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val TEST = false

object AiConnector {
    private val timeout = 7.seconds.toJavaDuration()

    private val logger = ktx.log.logger<AiConnector>()

    val model: ChatLanguageModel by lazy {
        try {
            if (TEST) {
                logger.info { "Using mock AI model for testing" }
                MockChatLanguageModel()
            } else {
                val apiKey = Secrets.get("OPENAI_API_KEY")
                requireNotNull(apiKey) { "OPENAI_API_KEY must be set" }
                logger.info { "Initializing OpenAI model" }
                OpenAiChatModel
                    .OpenAiChatModelBuilder()
                    .modelName("gpt-4o-mini")
                    .temperature(0.7)
                    .maxCompletionTokens(250)
                    .apiKey(apiKey)
                    .logRequests(true)
                    .logResponses(true)
                    .strictJsonSchema(true)
                    .timeout(timeout)
                    .build()
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to initialize ChatLanguageModel: ${e.message}" }
            // Fallback to mock model in case of initialization failure
            MockChatLanguageModel()
        }
    }

    val moderationModel: ModerationModel by lazy {
        try {
            val apiKey = Secrets.get("OPENAI_API_KEY")
            requireNotNull(apiKey) { "OPENAI_API_KEY must be set" }
            logger.info { "Initializing OpenAI model" }
            OpenAiModerationModel
                .builder()
                .modelName("omni-moderation-latest")
                .apiKey(apiKey)
                .logRequests(true)
                .logResponses(true)
                .timeout(timeout)
                .build()
        } catch (e: Exception) {
            logger.error(e) { "Failed to initialize ModerationModel: ${e.message}" }
            throw e
        }
    }
}
