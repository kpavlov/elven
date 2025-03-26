package me.kpavlov.elven.ai

import dev.langchain4j.service.MemoryId
import dev.langchain4j.service.Moderate
import dev.langchain4j.service.TokenStream
import dev.langchain4j.service.UserMessage

interface Assistant {
    @Moderate
    fun chat(
        @MemoryId playerName: String,
        @UserMessage userMessage: String,
    ): Reply

    @Moderate
    fun streamChat(
        @MemoryId playerName: String,
        @UserMessage userMessage: String,
    ): TokenStream
}
