package me.kpavlov.elven.characters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import me.kpavlov.elven.ChatController
import me.kpavlov.elven.ai.AiStrategy

@Suppress("LongParameterList")
abstract class AiCharacter(
    name: String,
    folderName: String = name,
    x: Float = 0f,
    y: Float = 0f,
    width: Int,
    height: Int,
    speed: Number = 10,
    run: Boolean = false,
) : AbstractCharacter(
        name = name,
        folderName = folderName,
        x = x,
        y = y,
        width = width,
        height = height,
        speed = speed,
        run = run,
    ) {
    private val aiStrategy = AiStrategy(name)
    private var sound: Sound? = null

    fun ask(
        question: String,
        from: PlayerCharacter? = null,
    ) {
//        GlobalScope.launch(Dispatchers.IO) {
//            async {
        from?.let {
            ChatController.say(it, question)
        }
        val answer = aiStrategy.reply(question)
        ChatController.say(this@AiCharacter, answer)
        logger.info {
            answer
        }
//            }
//        }
    }

    fun onMeetPlayer(player: PlayerCharacter) {
        try {
            if (sound == null) {
                sound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/orc/hey.mp3"))
            }
            sound?.play(1f)
        } catch (e: Exception) {
            logger.error(e) { "Cannot play sound" }
        }
    }

    override fun dispose() {
        super.remove()
        sound?.dispose()
    }
}
