package me.kpavlov.langchain4j

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.output.Response
import java.util.concurrent.ConcurrentHashMap

private val genericResponses =
    arrayOf(
        "Интересно.",
        "Трудно сказать.",
        "Это сложный вопрос.",
        "Нужно подумать.",
        "Это возможно.",
        "Подумаем об этом.",
        "Я не уверен.",
        "Может быть.",
    )

/**
 * LangChain4J ChatLanguageModel providing expected response to expected request.
 * Expected requests can be added dynamically. Follow wiremock interface
 */
class MockChatLanguageModel : ChatLanguageModel {
    private val expectedResponses = ConcurrentHashMap<String, String>()

    /**
     * Generates a response from the model based on a sequence of messages.
     * Typically, the sequence contains messages in the following order:
     * System (optional) - User - AI - User - AI - User ...
     *
     * @param messages A list of messages.
     * @return The response generated by the model.
     */
    override fun generate(messages: List<ChatMessage>): Response<AiMessage> {
        val lastUserMessage = messages.filterIsInstance<UserMessage>().lastOrNull()
        return if (lastUserMessage != null) {
            Thread.sleep(500)
            val responseText = expectedResponses[lastUserMessage.singleText()] ?: genericResponses.random()
            Response(AiMessage(responseText))
        } else {
            Response(AiMessage("No user message found"))
        }
    }

    /**
     * Adds an expected response for a given user request.
     * @param request The user request.
     * @param response The expected response.
     */
    fun addExpectedResponse(
        request: String,
        response: String,
    ) {
        expectedResponses[request] = response
    }

    /**
     * Removes an expected response for a given user request.
     * @param request The user request.
     */
    fun removeExpectedResponse(request: String) {
        expectedResponses.remove(request)
    }

    /**
     * Clears all the expected responses.
     */
    fun clearExpectedResponses() {
        expectedResponses.clear()
    }
}
