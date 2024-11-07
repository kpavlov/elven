package me.kpavlov.elven

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

/** This represents a rich, deep green often seen in forested areas. */
val LUSH_FOREST_GREEN = Color(0.133333f, 0.545098f, 0.133333f, 1f)

/**
 * A slightly darker and muted green to represent mossy, shaded areas in a
 * forest.
 */
val MOSSY_GREEN = Color(0.333333f, 0.419608f, 0.184314f, 1f)

/**
 * This works well for forest ground or paths in combination with the green
 * foliage.
 */
val EARTHY_BROWN = Color(0.396078f, 0.262745f, 0.129412f, 1f)

val BLUE_BACKGROUND = createBackground(Color.BLUE)
val RED_BACKGROUND = createBackground(Color.RED)

private fun createBackground(color: Color): Drawable {
    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fill()
    val texture = Texture(pixmap)
    pixmap.dispose()
    return TextureRegionDrawable(TextureRegion(texture))
}
