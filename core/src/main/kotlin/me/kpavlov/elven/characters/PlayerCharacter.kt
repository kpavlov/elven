package me.kpavlov.elven.characters

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
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
    width: Float = 1f,
    height: Float = 1f,
    speed: Float = .05f,
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

        if (input.isTouched) { // If the user has clicked or tapped the screen
            logger.info("Don't touch me!")
        }
    }
}
