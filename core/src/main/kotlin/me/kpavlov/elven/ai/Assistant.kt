package me.kpavlov.elven.ai

import dev.langchain4j.service.MemoryId
import dev.langchain4j.service.Moderate
import dev.langchain4j.service.UserMessage
import dev.langchain4j.service.V

interface Assistant {
    @Moderate
    fun chat(
        @MemoryId playerName: String,
        @UserMessage userMessage: String,
        @V("coins") coins: Int,
    ): Reply
}
