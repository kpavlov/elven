package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin

private const val FONT_PATH = "ui/fonts/Roboto-Regular.ttf"

private const val CYRILLIC_ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя"

// Utility class to manage font generation
class FontGenerator(
    fontPath: String,
) {
    private val generator = FreeTypeFontGenerator(Gdx.files.internal(fontPath))

    fun generateFont(
        size: Int,
        color: com.badlogic.gdx.graphics.Color = com.badlogic.gdx.graphics.Color.YELLOW,
    ): BitmapFont =
        generator.generateFont(
            FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = size
                this.color = color
                this.characters = FreeTypeFontGenerator.DEFAULT_CHARS + CYRILLIC_ALPHABET
                // Add additional parameters as needed
                // borderWidth = 1f
                // borderColor = Color.BLACK
                // shadowOffsetX = 1
                // shadowOffsetY = 1
                // shadowColor = Color.BLACK
            },
        )

    fun dispose() {
        generator.dispose()
    }
}

// Extension function to add fonts to skin
fun Skin.addFonts(fontPath: String = FONT_PATH) {
    val fontGen = FontGenerator(fontPath)

    // Generate different font sizes
    add("small", fontGen.generateFont(12))
    add("default", fontGen.generateFont(16))
    add("medium", fontGen.generateFont(20))
    add("large", fontGen.generateFont(24))
    add("title", fontGen.generateFont(32))

    // Cleanup
    fontGen.dispose()
}

data object DefaultFonts {
    val defaultFont: BitmapFont
    val small: BitmapFont
    val medium: BitmapFont
    val large: BitmapFont
    val title: BitmapFont

    init {
        val generator = FontGenerator(FONT_PATH)
        defaultFont = generator.generateFont(16)
        small = generator.generateFont(12)
        medium = generator.generateFont(20)
        large = generator.generateFont(24)
        title = generator.generateFont(32)
        generator.dispose()
    }
}
