package me.kpavlov.elven

import com.badlogic.gdx.graphics.g2d.BitmapFont
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.freetype.async.loadFreeTypeFontAsync
import ktx.freetype.async.loadFreeTypeFontSync
import ktx.freetype.async.registerFreeTypeFontLoaders

fun initiateAssetStorage(): AssetStorage {
    // Coroutines have to be enabled.
    // This has to be called on the main rendering thread:
    KtxAsync.initiate()

    val assetStorage = AssetStorage()
    // Registering TTF/OTF file loaders:
    assetStorage.registerFreeTypeFontLoaders()

    // AssetStorage is now ready to load FreeType fonts.
    return assetStorage
}

fun AssetStorage.loadFontAsync(fontPath: String) {
    // Scheduling font loading:
    val deferred = this.loadFreeTypeFontAsync(fontPath)
    // Launching a coroutine:
    val job =
        KtxAsync.launch {
            // Waiting until the font is loaded:
            val font: BitmapFont = deferred.await()
            // Font is now ready to use.
        }
}

fun AssetStorage.loadFont(fontPath: String): BitmapFont {
    // Scheduling font loading:
    val font: BitmapFont = this.loadFreeTypeFontSync(fontPath)
    return font
}
