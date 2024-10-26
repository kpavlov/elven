package me.kpavlov.elven.characters

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Logger

@Suppress("LongParameterList")
abstract class AbstractCharacter(
    name: String,
    val texture: Texture,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 1f,
    height: Float = 1f,
    var speed: Float = .05f,
    var run: Boolean = false,
) : Actor() {
    protected val logger = Logger(name)

    val sprite: Sprite = Sprite(texture)

    init {
        this.name = name
    }

    private fun actualSpeed() =
        if (this.run) {
            speed * 2
        } else {
            speed
        }

    init {
        sprite.setSize(width, height)
        sprite.x = x
        sprite.y = y
    }

    fun render(spriteBatch: SpriteBatch) {
        sprite.draw(spriteBatch)
//        spriteBatch.draw(texture, x, y, 1f, 1f)
    }

    fun moveTo(
        x: Number,
        y: Number,
    ) {
        sprite.setPosition(x.toFloat(), y.toFloat())
    }

    fun moveWest() {
        sprite.translateX(-actualSpeed())
    }

    fun moveEast() {
        sprite.translateX(+actualSpeed())
    }

    fun moveSouth() {
        sprite.translateY(-actualSpeed())
    }

    fun moveNorth() {
        sprite.translateY(+actualSpeed())
    }

    fun dispose() {
        texture.dispose()
    }
}
