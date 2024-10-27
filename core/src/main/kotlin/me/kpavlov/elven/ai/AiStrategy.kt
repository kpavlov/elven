package me.kpavlov.elven.ai

import com.badlogic.gdx.Gdx
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import me.kpavlov.elven.ai.AiConnector.model

class AiStrategy(
    name: String,
) {
    val systemPrompt: String

    init {
        val file = Gdx.files.classpath("ai/characters/$name/system-prompt.md")
        systemPrompt = file.readString()
    }

    fun reply(question: String): String {
        val result =
            model.generate(
                listOf(
                    SystemMessage.systemMessage(systemPrompt),
                    UserMessage.from("Reply to:```$question```. Never stop on unfinished sentence"),
                ),
            )
        return result.content().text()
    }
}
