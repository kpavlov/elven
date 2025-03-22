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
val TURQUOISE = Color(0.64f, 0.224f, 0.208f, 1f)

/**
 * This works well for forest ground or paths in combination with the green
 * foliage.
// */
val EARTHY_BROWN = Color(0.396078f, 0.262745f, 0.129412f, 1f)

val BLUE_BACKGROUND = createBackground(Color.BLUE)
val RED_BACKGROUND = createBackground(Color.RED)
val GREEN_BORDER = createBorder(LUSH_FOREST_GREEN, 0)

val NPC_COLOR: Color = Color.CORAL
val PLAYER_COLOR: Color = Color.valueOf("40e0d0")

private fun createBackground(color: Color): Drawable {
    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fill()
    val texture = Texture(pixmap)
    pixmap.dispose()
    return TextureRegionDrawable(TextureRegion(texture))
}

/**
 * Creates a drawable with a colored border that can be used as a background
 * for UI elements.
 *
 * @param color The color of the border
 * @param borderWidth The width of the border in pixels
 * @param cornerRadius The radius of the rounded corners (0 for sharp corners)
 * @return A drawable with the specified border
 */
fun createBorder(
    color: Color,
    borderWidth: Int,
    cornerRadius: Int = 0,
): Drawable {
    // Create a pixmap with dimensions that allow for the border
    val size = 10 // Base size - will be stretched when applied
    val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)

    // Set the color for the border
    pixmap.setColor(color)

    // Draw the border - for a simple implementation, we'll draw a rectangle outline
    if (cornerRadius > 0) {
        // Draw rounded rectangle border
        pixmap.drawRectangle(
            borderWidth / 2,
            borderWidth / 2,
            size - borderWidth,
            size - borderWidth,
        )
    } else {
        // Draw rectangle border by drawing lines
        // Top line
        pixmap.fillRectangle(0, 0, size, borderWidth)
        // Bottom line
        pixmap.fillRectangle(0, size - borderWidth, size, borderWidth)
        // Left line
        pixmap.fillRectangle(0, borderWidth, borderWidth, size - 2 * borderWidth)
        // Right line
        pixmap.fillRectangle(size - borderWidth, borderWidth, borderWidth, size - 2 * borderWidth)
    }

    // Create a texture from the pixmap
    val texture = Texture(pixmap)
    pixmap.dispose()

    // Return as a drawable
    return TextureRegionDrawable(TextureRegion(texture))
}
