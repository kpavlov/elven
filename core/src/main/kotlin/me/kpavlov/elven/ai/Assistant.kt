package me.kpavlov.elven.ai

interface Assistant {
    fun chat(userMessage: String): String
}
