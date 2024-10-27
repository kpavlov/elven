package me.kpavlov.elven.characters

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import me.kpavlov.elven.ChatWindow
import me.kpavlov.elven.Controls
import kotlin.math.abs
import kotlin.math.max

/**
 * Player Characters (PCs) – These are the characters controlled by the player (the user).
 * The player makes decisions for the character, such as actions, movement, and dialogue choices.
 */
@Suppress("LongParameterList")
abstract class PlayerCharacter(
    name: String,
    folderName: String = name,
    x: Float = 0f,
    y: Float = 0f,
    width: Int,
    height: Int,
    speed: Number = 10,
    run: Boolean = false,
    val controls: Controls,
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
    private var meetingWith: AiCharacter? = null

    private val collisionDistance = max(width, height)

    init {
        addListener(PlayerInputListener())
//        addListener(HitListener())
//        addListener(
//            object : ChangeListener() {
//                override fun changed(
//                    event: ChangeEvent,
//                    actor: Actor,
//                ) {
//                    // TODO("Not yet implemented")
//                }
//            },
//        )
    }

    fun reactOnControls(input: Input) {
        val elf = this
        elf.run = input.isKeyPressed(controls.keyRun)

        if (input.isKeyPressed(controls.keyRight)) {
            elf.moveEast()
            lookAtMe()
        }

        if (input.isKeyPressed(controls.keyLeft)) {
            elf.moveWest()
            lookAtMe()
        }
        if (input.isKeyPressed(controls.keyUp)) {
            elf.moveNorth()
            lookAtMe()
        }
        if (input.isKeyPressed(controls.keyDown)) {
            elf.moveSouth()
            lookAtMe()
        }
    }

    /**
     * Checks if this actor has collided with another actor and handles the collision response.
     *
     * @param other The other actor to check collision against.
     */
    fun checkHit(other: Actor) {
        if (meetingWith === other) {
            if (abs(this.x - other.x) > collisionDistance ||
                abs(this.y - other.y) > collisionDistance
            ) {
                onLeaveAiCharacter(other)
                meetingWith = null
            }
            return
        }

        if (other is AiCharacter &&
            abs(this.x - other.x) < collisionDistance &&
            abs(this.y - other.y) < collisionDistance
        ) {
            meetingWith = other
            onMeetAiCharacter(other)
        }
    }

    private fun onMeetAiCharacter(other: AiCharacter) {
        ChatWindow.startDialog(this, other)
        val question = "Hello, my name is $name. How are you doing?"
        ChatWindow.say(this, question)
        other.onMeetPlayer(this)
        // other.ask(from = this, question = question)
    }

    private fun onLeaveAiCharacter(other: AiCharacter) {
        val bye = "Bye, ${other.name}"
        // other.ask(from = this, question = bye)
    }

    private fun lookAtMe() {
        val local3 = Vector3(x, y, 0f)
        val local2 = Vector2(x, y)
        val screen2 = localToScreenCoordinates(local2)
        val screen3 = Vector3(screen2.x, screen2.y, 0f)
//        lookAt(stage.stageToScreenCoordinates(Vector2(x, y)))
//        logger.info { "Look at $screen3" }
//        stage.camera.lookAt(screen3)
    }

    private inner class PlayerInputListener : ClickListener() {
        override fun touchDown(
            event: InputEvent,
            x: Float,
            y: Float,
            pointer: Int,
            button: Int,
        ): Boolean {
            logger.info { "down: $x;$y $pointer $button" }

            return false
        }
    }

    private inner class HitListener : EventListener {
        override fun handle(event: Event): Boolean {
            if (event.target !== this@PlayerCharacter) {
                logger.info { "I crushed into: ${event.target}" }
            }
            return true
        }
    }
}
