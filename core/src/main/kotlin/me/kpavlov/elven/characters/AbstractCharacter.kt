package me.kpavlov.elven.characters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import ktx.log.Logger
import me.kpavlov.elven.ChatController

@Suppress("LongParameterList")
abstract class AbstractCharacter(
    name: String,
    folderName: String,
    x: Float = 0f,
    y: Float = 0f,
    width: Int = 64,
    height: Int = 64,
    var speed: Number = 10,
    var run: Boolean = false,
) : Actor() {
    val logger = Logger(name)

    private val texture = Texture("characters/$folderName/texture.png")
    private val avatar =
        if (Gdx.files.internal("characters/$folderName/avatar.png").exists()) {
            Texture("characters/$folderName/avatar.png")
        } else {
            texture
        }
    private val region = TextureRegion(texture)

    init {
        this.name = name

        setSize(width.toFloat(), height.toFloat())
        isVisible = true
        setColor(0.7f, .6f, .3f, 1f)
        setPosition(x, y)

        addListener(
            object : InputListener() {
                override fun touchDown(
                    event: InputEvent?,
                    x: Float,
                    y: Float,
                    pointer: Int,
                    button: Int,
                ): Boolean {
                    sayHey()
                    return true
                }
            },
        )
    }

    private fun actualSpeed(): Float =
        if (this.run) {
            speed.toFloat() * 2
        } else {
            speed.toFloat()
        }

    override fun draw(
        batch: Batch,
        parentAlpha: Float,
    ) {
        val color = color
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
    }

    fun moveTo(
        x: Number,
        y: Number,
    ) {
        addAction(Actions.moveTo(x.toFloat(), y.toFloat()))
    }

    fun moveWest() {
        addAction(Actions.moveBy(-actualSpeed(), 0f, .5f))
    }

    fun moveEast() {
        addAction(Actions.moveBy(actualSpeed(), 0f, .5f))
    }

    fun moveSouth() {
        addAction(Actions.moveBy(0f, -actualSpeed(), .5f))
    }

    fun moveNorth() {
        addAction(Actions.moveBy(0f, actualSpeed(), .5f))
    }

    open fun dispose() {
        texture.dispose()
    }

    protected fun sayHey() {
//        addAction(Actions.)
        val greeting = "Привет, я $name"
        ChatController.say(this@AbstractCharacter, greeting)
        logger.info { greeting }
    }
}
