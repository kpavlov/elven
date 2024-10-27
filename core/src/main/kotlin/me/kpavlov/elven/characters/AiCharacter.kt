package me.kpavlov.elven.characters

import com.badlogic.gdx.graphics.Texture
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.kpavlov.elven.ChatController
import me.kpavlov.elven.ai.AiStrategy

@Suppress("LongParameterList")
abstract class AiCharacter(
    name: String,
    texture: Texture,
    x: Float = 0f,
    y: Float = 0f,
    width: Int,
    height: Int,
    speed: Number = 10,
    run: Boolean = false,
) : AbstractCharacter(
        name = name,
        texture = texture,
        x = x,
        y = y,
        width = width,
        height = height,
        speed = speed,
        run = run,
    ) {
    private val aiStrategy = AiStrategy(name)

    fun ask(
        question: String,
        from: PlayerCharacter? = null,
    ) {
        GlobalScope.launch {
            async {
                from?.let {
                    ChatController.say(it, question)
                }
                val answer = aiStrategy.reply(question)
                ChatController.say(this@AiCharacter, answer)
                logger.info {
                    answer
                }
            }
        }
    }
}
