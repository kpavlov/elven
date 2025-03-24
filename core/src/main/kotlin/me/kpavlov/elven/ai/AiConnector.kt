package me.kpavlov.elven.ai

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.chat.StreamingChatLanguageModel
import dev.langchain4j.model.moderation.ModerationModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiModerationModel
import dev.langchain4j.model.openai.OpenAiStreamingChatModel
import me.kpavlov.elven.utils.Secrets
import me.kpavlov.langchain4j.MockChatLanguageModel
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val TEST = false

object AiConnector {
    private val timeout = 15.seconds.toJavaDuration()
    private const val MAX_TOKENS = 1500
    private const val TEMPERATURE = 0.7
    private const val MODEL_NAME = "gpt-4o-mini"
    private const val MODERATION_MODEL_NAME = "omni-moderation-latest"
    private const val LOG_REQUESTS_RESPONSES = true

    private val logger = ktx.log.logger<AiConnector>()

    private val apiKey = requireNotNull(Secrets.get("OPENAI_API_KEY")) { "OPENAI_API_KEY must be set" }

    val model: ChatLanguageModel by lazy {
        try {
            if (TEST) {
                logger.info { "Using mock AI model for testing" }
                MockChatLanguageModel()
            } else {

                OpenAiChatModel
                    .builder()
                    .modelName(MODEL_NAME)
                    .temperature(TEMPERATURE)
                    .maxCompletionTokens(MAX_TOKENS)
                    .apiKey(apiKey)
                    .logRequests(LOG_REQUESTS_RESPONSES)
                    .logResponses(LOG_REQUESTS_RESPONSES)
                    .responseFormat("json_schema")
                    .strictJsonSchema(true)
                    .timeout(timeout)
                    .build()
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to initialize ChatLanguageModel: ${e.message}" }
            throw e
        }
    }

    val streamingModel: StreamingChatLanguageModel by lazy {
        try {
            OpenAiStreamingChatModel
                .builder()
                .modelName(MODEL_NAME)
                .temperature(0.5)
                .apiKey(apiKey)
                .maxCompletionTokens(MAX_TOKENS)
                .logRequests(LOG_REQUESTS_RESPONSES)
                .logResponses(LOG_REQUESTS_RESPONSES)
                .responseFormat("json_schema")
                .strictJsonSchema(true)
                .timeout(timeout)
                .build()
        } catch (e: Exception) {
            logger.error(e) { "Failed to initialize ChatLanguageModel: ${e.message}" }
            // Fallback to mock model in case of initialization failure
            throw e
        }
    }

    val moderationModel: ModerationModel by lazy {
        try {
            OpenAiModerationModel
                .builder()
                .modelName(MODERATION_MODEL_NAME)
                .apiKey(apiKey)
                .logRequests(LOG_REQUESTS_RESPONSES)
                .logResponses(LOG_REQUESTS_RESPONSES)
                .timeout(timeout)
                .build()
        } catch (e: Exception) {
            logger.error(e) { "Failed to initialize ModerationModel: ${e.message}" }
            throw e
        }
    }
}
