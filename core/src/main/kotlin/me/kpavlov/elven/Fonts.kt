package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import ktx.style.set

// private const val FONT_PATH = "fonts/Roboto-Regular.ttf"
// private const val FONT_PATH = "fonts/NotoSans-Regular.ttf"
private const val FONT_PATH = "fonts/NotoSans-Medium.ttf"

private const val CYRILLIC_ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя"

// Utility class to manage font generation
class FontGenerator(
    fontPath: String,
) : Disposable {
    private val generator = FreeTypeFontGenerator(Gdx.files.internal(fontPath))

    fun generateFont(
        size: Int,
        color: com.badlogic.gdx.graphics.Color = com.badlogic.gdx.graphics.Color.WHITE,
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

    override fun dispose() {
        generator.dispose()
    }
}

// Extension function to add fonts to skin
fun Skin.addFonts(fontPath: String = FONT_PATH) {
    val fontGen = FontGenerator(fontPath)

    val defaultSize = fontGen.generateFont(16)
    // Generate different font sizes
    val small = fontGen.generateFont(12)
    val medium = fontGen.generateFont(20)
    val large = fontGen.generateFont(24)
    val title = fontGen.generateFont(32)

    set("default-font", defaultSize)
    set("default", medium)
    set("font", medium)

    set("small", small)
    set("list", medium)
    set("subtitle", large)
    set("window", title)

    set("medium", medium)
    set("large", large)
    set("title", title)

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
        try {
            small = generator.generateFont(12)
            medium = generator.generateFont(20)
            large = generator.generateFont(24)
            title = generator.generateFont(32)
            defaultFont = medium
        } finally {
            generator.dispose()
        }
    }
}
