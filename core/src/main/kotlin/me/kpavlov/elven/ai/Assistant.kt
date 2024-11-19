package me.kpavlov.elven.ai

import dev.langchain4j.service.UserMessage

interface Assistant {
    fun chat(
        @UserMessage userMessage: String,
    ): String
}
