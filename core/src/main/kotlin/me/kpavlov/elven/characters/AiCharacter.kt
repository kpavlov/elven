package me.kpavlov.elven.characters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.kpavlov.elven.ai.AiStrategy

@Suppress("LongParameterList")
abstract class AiCharacter(
    name: String,
    private val folderName: String = name,
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

    @OptIn(
        DelicateCoroutinesApi::class,
    )
    fun ask(
        question: String,
        from: PlayerCharacter,
        callback: (String) -> Unit,
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            launch {
                val answer = aiStrategy.reply(question)
                callback(answer)
            }
        }
    }

    fun onMeetPlayer(player: PlayerCharacter) {
        try {
            if (sound == null) {
                sound = Gdx.audio.newSound(Gdx.files.internal("characters/$folderName/hey.mp3"))
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
