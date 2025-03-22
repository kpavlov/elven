package me.kpavlov.elven

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import me.kpavlov.elven.characters.Crab
import me.kpavlov.elven.characters.Dwarf
import me.kpavlov.elven.characters.Orc
import me.kpavlov.elven.characters.Robin
import me.kpavlov.elven.screens.game.GameScreen

/**
 * [com.badlogic.gdx.ApplicationListener] implementation shared by all
 * platforms.
 */
class Main : ApplicationAdapter() {
    private lateinit var assetStorage: AssetStorage
    private lateinit var batch: Batch
    private lateinit var viewport: Viewport
    private lateinit var touchPos: Vector2
    private lateinit var map: TiledMap
    private lateinit var camera: OrthographicCamera
    private lateinit var audioController: AudioManager
    private lateinit var myFont: BitmapFont
    private lateinit var gameScreen: GameScreen
    private var isPaused = false

    override fun create() {
        KtxAsync.initiate()
        assetStorage = initiateAssetStorage()
        myFont = DefaultFonts.defaultFont

        initSkin()

        audioController = AudioManager

        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        // Initialize game screen
        gameScreen = GameScreen(camera, myFont)

        // Create characters
        val player = Robin()
        val orc = Orc()
        val crab = Crab()
        val dwarf = Dwarf()

        // Position characters
        player.setPosition(730f, 280f)
        crab.setPosition(250f, 300f)
        dwarf.setPosition(140f, 320f)
        orc.setPosition(450f, 280f)

        // Add characters to game screen
        gameScreen.addCharacters(listOf(player, orc, crab, dwarf))

        touchPos = Vector2()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        gameScreen.resize(width, height)
    }

    override fun render() {
        if (isPaused) return
        gameScreen.input()
        gameScreen.logic()
        gameScreen.draw()
    }

    override fun pause() {
        isPaused = true
        AudioManager.stopMusic()
    }

    override fun resume() {
        isPaused = false
        AudioManager.playMusic()
    }

    override fun dispose() {
        gameScreen.dispose()
        map.dispose()
        audioController.dispose()
    }
}
