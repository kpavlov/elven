package me.kpavlov.elven

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Camera
import me.kpavlov.elven.characters.AbstractCharacter

class CameraController(
    val camera: Camera,
    val input: Input,
    val controls: Controls = arrowControls,
    var actor: AbstractCharacter? = null,
) : InputAdapter() {
    fun reactOnControls() {
        if (actor != null) {
            camera.position.x = actor!!.x
            camera.position.y = actor!!.y
        } else {
            var dX = 0f
            var dY = 0f
            var speed = 1f
            if (input.isKeyPressed(controls.keyRun)) {
                speed *= 2
            }
            if (input.isKeyPressed(controls.keyRight)) {
                dX += speed
            }

            if (input.isKeyPressed(controls.keyLeft)) {
                dX -= speed
            }
            if (input.isKeyPressed(controls.keyUp)) {
                dY += speed
            }
            if (input.isKeyPressed(controls.keyDown)) {
                dY -= speed
            }
            camera.translate(dX, dY, 0f)
        }
    }
}
