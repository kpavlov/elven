package me.kpavlov.elven.characters

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
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
    texture: Texture,
    x: Float = 0f,
    y: Float = 0f,
    width: Int,
    height: Int,
    speed: Number = 10,
    run: Boolean = false,
    val controls: Controls,
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
    private var meetingWith: AiCharacter? = null

    private val collisionDistance = max(width, height)

    init {
        addListener(PlayerInputListener())
        addListener(HitListener())
    }

    fun reactOnControls(input: Input) {
        val elf = this
        elf.run = input.isKeyPressed(controls.keyRun)

        if (input.isKeyPressed(controls.keyRight)) {
            elf.moveEast()
        }

        if (input.isKeyPressed(controls.keyLeft)) {
            elf.moveWest()
        }
        if (input.isKeyPressed(controls.keyUp)) {
            elf.moveNorth()
        }
        if (input.isKeyPressed(controls.keyDown)) {
            elf.moveSouth()
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
                logger.info { "Bye, ${other.name}" }
                meetingWith = null
            }
            return
        }

        if (other is AiCharacter &&
            abs(this.x - other.x) < collisionDistance &&
            abs(this.y - other.y) < collisionDistance
        ) {
            meetingWith = other
            val question = "Hello, my name is $name. How are you doing?"
            logger.info { question }
            other.ask(from = this@PlayerCharacter, question = question) // todo: better comm
        }
    }

    private inner class PlayerInputListener : InputListener() {
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
