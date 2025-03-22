package me.kpavlov.elven.ai

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.TextContent
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.internal.Json
import dev.langchain4j.memory.chat.ChatMemoryProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore
import me.kpavlov.elven.characters.AiCharacter
import me.kpavlov.elven.characters.PlayerCharacter

private const val CHAT_MEMORY_WINDOW_SIZE = 100

class Memory(
    private val character: AiCharacter,
) {
    private val chatMemoryStore = InMemoryChatMemoryStore()

    val chatMemoryProvider =
        ChatMemoryProvider { memoryId ->
            MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(CHAT_MEMORY_WINDOW_SIZE)
                .chatMemoryStore(chatMemoryStore)
                .build()
        }

    fun getChatHistory(withPlayer: PlayerCharacter): List<ChatMessage> =
        chatMemoryProvider
            .get(withPlayer.name)
            .messages()
            .map {
                return@map when (it) {
                    is AiMessage -> {
                        val text = it.text()
                        if (text.isNullOrBlank()) {
                            return@map null
                        }
                        try {
                            Json.fromJson<Reply>(text, Reply::class.java)?.let {
                                return@map ChatMessage(from = character, text = it.text, coins = it.coins)
                            }
                        } catch (_: Exception) {
                            // can't deserialize
                            return@map ChatMessage(from = character, text = text, coins = 0)
                        }
                    }

                    is UserMessage -> {
                        val textContent = it.contents().firstOrNull() as TextContent?
                        textContent?.text()?.let { text ->
                            val userMessage =
                                text.substringBefore(
                                    "\n\nAnswer using the following information:\n",
                                )
                            ChatMessage(from = withPlayer, text = userMessage, coins = 0)
                        }
                    }

                    else -> {
                        null
                    }
                }
            }.filterNotNull()
}
