package me.kpavlov.elven.characters

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture

private val controls = Controls(
    keyUp = Keys.W,
    keyLeft = Keys.A,
    keyRight = Keys.D,
    keyDown = Keys.S,
    keyRun = Keys.SHIFT_LEFT,
)

class Dwarf: PlayerCharacter(
    name = "Dawlin",
    texture = Texture("characters/dwarf.png"),
    speed = .01f,
    width = 0.7f,
    height = 0.7f,
    controls = controls
) {

}
