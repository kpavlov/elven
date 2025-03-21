package me.kpavlov.elven.characters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import me.kpavlov.elven.Controls
import me.kpavlov.elven.screens.game.ChatWindow
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
    coins: Int = 0,
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
        coins = coins,
    ) {
    private var meetingWith: AiCharacter? = null

    private val collisionDistance = max(width, height)
    private val greeting: String

    init {
        addListener(PlayerInputListener())
        val greetingFile = Gdx.files.internal("characters/$folderName/greeting.txt")
        greeting =
            if (greetingFile.exists()) {
                greetingFile.readString()
            } else {
                "Hello, my name is $name. How are you doing?"
            }
//        addListener(HitListener())
        addListener(
            object : ChangeListener() {
                override fun changed(
                    event: ChangeEvent,
                    actor: Actor,
                ) {
                    println("Actor: ${actor.name} - changed $event")
                }
            },
        )
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
        if (!other.isVisible) {
            return
        }
        if (other.stage != this.stage) {
            return
        }
        ChatWindow.startDialog(this, other)
        if (other.chatHistoryWithPlayer(this).isEmpty()) {
            ChatWindow.say(this, greeting)
        } else {
            ChatWindow.say(this, "Hi, Again!")
        }
        other.onMeetPlayer(this)
    }

    private fun onLeaveAiCharacter(other: AiCharacter) {
        // Placeholder for future implementation
        // Example: other.ask(from = this, question = "Bye, ${other.name}")
    }

    private fun lookAtMe() {
        // Placeholder for camera focusing functionality
        // This method will be implemented in the future to make the camera follow the player
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
