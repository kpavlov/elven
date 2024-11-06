package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.Scene2DSkin

// https://stackoverflow.com/questions/39053637/how-to-use-freefonttype-with-libgdx-skin-and-assetmanager
fun initSkin() {
    val textureAtlas = TextureAtlas("skin/uiskin.atlas")
    val loadedSkin = Skin(textureAtlas)
    loadedSkin.addFonts()
    loadedSkin.load(Gdx.files.internal("skin/custom-uiskin.json"))
    Scene2DSkin.defaultSkin = loadedSkin
}
