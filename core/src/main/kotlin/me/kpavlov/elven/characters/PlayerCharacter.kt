package me.kpavlov.elven.characters

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import me.kpavlov.elven.Controls

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
    init {
        addListener(PlayerInputListener())
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
}
