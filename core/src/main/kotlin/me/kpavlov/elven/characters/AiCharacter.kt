package me.kpavlov.elven.characters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newAsyncContext
import me.kpavlov.elven.AudioManager
import me.kpavlov.elven.ai.AiStrategy
import me.kpavlov.elven.ai.ChatMessage
import me.kpavlov.elven.ai.Reply

private val executor = newAsyncContext(10, "ai-thread")

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
    coins: Int = 0,
    val streamingResponses: Boolean = false,
    val tools: List<Any> = emptyList(),
) : AbstractCharacter(
        name = name,
        folderName = folderName,
        x = x,
        y = y,
        width = width,
        height = height,
        speed = speed,
        run = run,
        coins = coins,
    ) {
    private val aiStrategy = AiStrategy(this)
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
        onStart: (Reply) -> Unit,
        onPartialResponse: (String) -> Unit = { },
        onCompleteResponse: (Reply) -> Unit = {},
    ) {
        onStart(Reply("..."))
        if (streamingResponses) {
            aiStrategy.streamingReply(
                aiCharacter = this@AiCharacter,
                player = from,
                question = question,
                onPartialResponse = { partial ->
                    onPartialResponse(partial)
                },
                onCompleteResponse = { reply ->
                    onCompleteResponse(reply)
                },
            )
        } else {
            KtxAsync.launch(executor) {
                val reply =
                    aiStrategy.reply(
                        aiCharacter = this@AiCharacter,
                        player = from,
                        question = question,
                    )
                if (reply.coins > 0) {
                    from.coins += reply.coins
                    coins -= reply.coins
                }
                onCompleteResponse(reply)
            }
        }
    }

    fun chatHistoryWithPlayer(player: PlayerCharacter): List<ChatMessage> = aiStrategy.getChatHistory(player)

    fun onMeetPlayer(player: PlayerCharacter) {
        sound?.apply(AudioManager::playSound)
    }

    override fun dispose() {
        super.dispose()
        sound?.dispose()
    }
}
