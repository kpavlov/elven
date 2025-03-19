package me.kpavlov.elven.ai

import me.kpavlov.elven.characters.AbstractCharacter

data class ChatMessage(
    val from: AbstractCharacter,
    val text: String,
)
