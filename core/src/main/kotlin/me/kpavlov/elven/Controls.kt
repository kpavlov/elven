package me.kpavlov.elven

import com.badlogic.gdx.Input.Keys

val arrowControls =
    Controls(
        keyUp = Keys.UP,
        keyLeft = Keys.LEFT,
        keyRight = Keys.RIGHT,
        keyDown = Keys.DOWN,
        keyRun = Keys.SHIFT_LEFT,
    )

val asdwControls =
    Controls(
        keyUp = Keys.W,
        keyLeft = Keys.A,
        keyRight = Keys.D,
        keyDown = Keys.S,
        keyRun = Keys.SHIFT_LEFT,
    )

data class Controls(
    val keyLeft: Int,
    val keyRight: Int,
    val keyUp: Int,
    val keyDown: Int,
    val keyRun: Int,
)
