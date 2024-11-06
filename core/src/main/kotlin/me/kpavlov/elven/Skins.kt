package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.scrollPane
import ktx.style.skin
import ktx.style.textButton
import ktx.style.textField
import ktx.style.window

val textureAtlas = TextureAtlas("skin/uiskin.atlas")

val mySkin = createSkin()

fun createSkin(): Skin =
    skin(textureAtlas) {
        val skin = it
        // Add BitmapFont
        val defaultFont = DefaultFonts.defaultFont
        skin.add("default", defaultFont)
        skin.add("font", defaultFont)
        skin.add("font-list", DefaultFonts.small)
        skin.add("font-subtitle", DefaultFonts.title)
        skin.add("font-window", DefaultFonts.large)

        // Add Colors
        skin.add("black", Color(0f, 0f, 0f, 1f))
        skin.add("disabled", Color(0.23137255f, 0.24705882f, 0.25882354f, 1f))
        skin.add("highlight", Color(0.19215687f, 0.6627451f, 0.95686275f, 1f))
        skin.add("message", Color(0.44313726f, 0.47843137f, 0.5372549f, 1f))
        skin.add("split", Color(0.44313726f, 0.47843137f, 0.5372549f, 1f))
        skin.add("white", Color(1f, 1f, 1f, 1f))

        window {
            titleFont = defaultFont
            titleFontColor = Color.WHITE
//            background = skin.getDrawable("window-bg")
//            stageBackground = skin.getDrawable("window-bg")
        }

        textField {
            font = defaultFont
            fontColor = Color.WHITE
        }

        textButton {
            font = defaultFont
        }

        scrollPane {
        }

        label {
            font = defaultFont
            fontColor = Color.WHITE
        }
        // Add TintedDrawable (example)
//    skin.add("split", Skin.TintedDrawable(skin.getDrawable("white"), skin.getColor("split")))
//    skin.add("black", Skin.TintedDrawable(skin.getDrawable("white"), skin.getColor("black")))
//    skin.add("highlight", Skin.TintedDrawable(skin.getDrawable("white"), skin.getColor("highlight")))

        // Add ButtonStyle
//        val buttonUpDrawable = skin.newDrawable("buttonUp")
//        val buttonOverDrawable = skin.newDrawable("buttonOver")
//        val buttonDownDrawable = skin.newDrawable("buttonDown")
//        val buttonDisabledDrawable = skin.newDrawable("buttonDisabled")

//    val buttonStyle =
//        TextButton.TextButtonStyle().apply {
//            up = buttonUpDrawable
//            over = buttonOverDrawable
//            down = buttonDownDrawable
//            disabled = buttonDisabledDrawable
//            font = defaultFont
//        }
//    skin.add("default", buttonStyle)

        // Add other styles similarly ...
        // For example, CheckBoxStyle, LabelStyle, ListStyle, etc.

        return skin
    }

// https://stackoverflow.com/questions/39053637/how-to-use-freefonttype-with-libgdx-skin-and-assetmanager
fun initSkin(myFont: BitmapFont) {
    val textureAtlas = TextureAtlas("skin/uiskin.atlas")
    val loadedSkin = Skin(Gdx.files.internal("skin/uiskin.json"), textureAtlas)
//    loadedSkin.textButton(name = "ru", extend = "default") { font = myFont }
//    loadedSkin.textField(name = "ru", extend = "default") { font = myFont }
//    loadedSkin.label(name = "ru", extend = "default") { font = myFont }
    loadedSkin.addFonts()
    Scene2DSkin.defaultSkin = mySkin
//
//        Scene2DSkin.defaultSkin.
//        skin {
//            addRegions(textureAtlas)
//            add("default-font", myFont)
//            add("font", myFont)
//            window {
// //                titleFont = myFont
//            }
//            textField {
// //                font = myFont
//                fontColor = com.badlogic.gdx.graphics.Color.WHITE
//            }
//            textButton { }
//            scrollPane { }
//            label { }
//        }
}
/*// Create modified default skin
fun skin(Scene2DSkin.defaultSkin) {
    // Modify Label style
    label {
        font = customFont
    }

    // Modify TextField style
    textField {
        font = customFont
        messageFontColor = com.badlogic.gdx.graphics.Color.GRAY
        fontColor = com.badlogic.gdx.graphics.Color.WHITE
    }

    // Modify TextArea style
    textArea {
        font = customFont
        messageFontColor = com.badlogic.gdx.graphics.Color.GRAY
        fontColor = com.badlogic.gdx.graphics.Color.WHITE
    }

    // Modify TextButton style
    textButton {
        font = customFont
    }

    // Modify CheckBox style
    checkBox {
        font = customFont
    }

    // Modify SelectBox style
    selectBox {
        font = customFont
        listStyle = defaultSkin.get<SelectBox.SelectBoxStyle>().listStyle
    }

    // Modify Window style
    window {
        titleFont = customFont
    }
}*/
