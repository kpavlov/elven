package me.kpavlov.elven.characters

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class AbstractCharacter(
    val name: String,
    val texture: Texture,
    x: Float = 0f,
    y: Float = 0f,
    private var width: Float = 1f,
    private var height: Float = 1f,
    var speed: Float = .05f,
    var run: Boolean = false,
) {

    private val sprite: Sprite = Sprite(texture)

    private fun actualSpeed() = if (this.run) {
        speed * 2
    } else {
        speed
    }

    init {
        sprite.setSize(width, height);
        sprite.x = x
        sprite.y = y
    }

    fun render(spriteBatch: SpriteBatch) {
        sprite.draw(spriteBatch);
//        spriteBatch.draw(texture, x, y, 1f, 1f)
    }

    fun moveTo(x: Number, y: Number) {
        sprite.setPosition(x.toFloat(),y.toFloat())
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