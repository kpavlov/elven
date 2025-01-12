package me.kpavlov.elven.characters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newAsyncContext
import ktx.async.onRenderingThread
import me.kpavlov.elven.AudioManager
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

    init {
        try {
            sound = Gdx.audio.newSound(Gdx.files.internal("characters/$folderName/hey.mp3"))
        } catch (_: Exception) {
            // ignore error
        }
    }

    fun ask(
        question: String,
        from: PlayerCharacter,
        callback: (String) -> Unit,
    ) {
        val executor = newAsyncContext(10, "ai-thread")
        KtxAsync.launch(executor) {
//            println("Executor context. ${isOnRenderingThread()}")
            val answer = aiStrategy.reply(question)
            onRenderingThread {
//                println("Main KTX context. ${isOnRenderingThread()}")
                callback(answer)
            }
//            println("Executor context. ${isOnRenderingThread()}")
        }
    }

    fun onMeetPlayer(player: PlayerCharacter) {
        sound?.apply(AudioManager::playSound)
    }

    override fun dispose() {
        super.remove()
        sound?.dispose()
    }
}
