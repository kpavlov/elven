package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.Scene2DSkin

fun initSkin() {
    val textureAtlas = TextureAtlas("skin/uiskin.atlas")
    val loadedSkin = Skin(Gdx.files.internal("skin/uiskin.json"), textureAtlas)
    Scene2DSkin.defaultSkin = loadedSkin
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
