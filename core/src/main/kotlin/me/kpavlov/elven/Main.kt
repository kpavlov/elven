package me.kpavlov.elven

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.actors.stage
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.scene2d.StageWidget
import ktx.scene2d.actor
import ktx.scene2d.actors
import me.kpavlov.elven.characters.AbstractCharacter
import me.kpavlov.elven.characters.Crab
import me.kpavlov.elven.characters.Orc
import me.kpavlov.elven.characters.PlayerCharacter
import me.kpavlov.elven.characters.Robin
import me.kpavlov.elven.screens.game.ChatWindow
import me.kpavlov.elven.screens.game.SettingsPanel

private val input = Gdx.input

private const val DEBUG = false

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
    private var mapWidth: Float = -1f
    private var mapHeight: Float = -1f
    private lateinit var settingsPanel: SettingsPanel

    private lateinit var camera: OrthographicCamera
    private lateinit var cameraController: CameraController

    //    private lateinit var mapRenderer: IsometricTiledMapRenderer
    private lateinit var mapRenderer: OrthogonalTiledMapRenderer
    private lateinit var stage: Stage
    private lateinit var audioController: AudioManager
    private lateinit var myFont: BitmapFont
    private var isPaused = false

    private val allCharacters = mutableListOf<AbstractCharacter>()
    private val playerCharacters = mutableListOf<PlayerCharacter>()

    override fun create() {
        KtxAsync.initiate()
        assetStorage = initiateAssetStorage()
        myFont = DefaultFonts.defaultFont

        initSkin()

        audioController = AudioManager

        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

//        map = TmxMapLoader().load("scenes/grass_and_water/isometric_grass_and_water.tmx")
//        mapRenderer = IsometricTiledMapRenderer(map)

        map = TmxMapLoader().load(GameConfig.MAP_FILE)
        val tileWidth = map.properties["tilewidth", Int::class.java]
        mapWidth = map.properties["width", Int::class.java].toFloat() * tileWidth
        val tileHeight = map.properties["tileheight", Int::class.java]
        mapHeight = map.properties["height", Int::class.java].toFloat() * tileHeight

        mapRenderer = OrthogonalTiledMapRenderer(map, 1f)

        // Set up the Stage and UI
        stage = stage(viewport = FillViewport(mapWidth, mapHeight, camera))

        settingsPanel = SettingsPanel(stage)
//        stage = stage(viewport = ScreenViewport(camera))
        Gdx.input.inputProcessor = stage // Make the stage receive input
        stage.isDebugAll = DEBUG

        batch = stage.batch
        viewport = stage.viewport

//        cameraController = CameraController(camera, input)
        ChatController.attachToStage(stage, myFont)
        ChatWindow.attachToStage(stage)

//        positionCameraAtMapCenter(camera, map)

        val player = Robin()
//        dwarf = Dwarf()
        val orc = Orc()
        val crab = Crab()
//        artur = Artur()

        touchPos = Vector2()

//        PlayerPanel(stage, player = player)

//        cameraController.actor = player

        player.setPosition(730f, 280f)
//        dwarf.setPosition(140f, 320f)
        crab.setPosition(250f, 300f)
        orc.setPosition(450f, 280f)
//        artur.setPosition(530f, 390f)

        stage.actors {
            addCharacter(crab, collection = allCharacters)
            addCharacter(orc, collection = allCharacters)

            /*

            actor(dwarf) {
                allCharacters += dwarf
            }
             */
            addCharacter(player, collection = playerCharacters)
        }
        constrainCameraToMap()
    }

    private fun <T : AbstractCharacter> StageWidget.addCharacter(
        character: T,
        collection: MutableCollection<T>,
    ) {
        this.actor(character) {
            character.gameMap = map
            collection += character
        }
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        stage.viewport.update(width, height, true)
    }

    override fun render() {
        if (isPaused) return
        input()
        logic()
        draw()
    }

    private fun input() {
        playerCharacters.forEach {
            it.reactOnControls(input)
        }
    }

    private fun logic() {
        playerCharacters.forEach { player ->
            allCharacters.forEach { other ->
                player.checkHit(other)
            }
        }
    }

    private fun draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Update camera and set the view for the map renderer
        camera.update()
        mapRenderer.setView(camera)

        // Render the isometric map
        mapRenderer.render()

        // Update and render the stage
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

//        constrainCameraToMap()
    }

    override fun pause() {
        isPaused = true
        audioController.stopMusic()
    }

    override fun resume() {
        isPaused = false
        audioController.playMusic()
    }

    override fun dispose() {
        playerCharacters.forEach { it.dispose() }
        allCharacters.forEach { it.dispose() }
        map.dispose()
        mapRenderer.dispose()
        stage.dispose()
        audioController.dispose()
    }

    fun positionCameraAtMapCenter(
        camera: OrthographicCamera,
        map: TiledMap,
    ) {
        val tileWidth = map.properties.get("tilewidth", Int::class.java).toFloat()
        val tileHeight = map.properties.get("tileheight", Int::class.java).toFloat()
        val mapWidthInTiles = map.properties.get("width", Int::class.java)
        val mapHeightInTiles = map.properties.get("height", Int::class.java)

        // Calculate map center in world coordinates for an isometric map
        val mapCenterX = (mapWidthInTiles + mapHeightInTiles) * (tileWidth / 2f) / 2f
        val mapCenterY = (mapHeightInTiles - mapWidthInTiles) * (tileHeight / 2f) / 2f

        // Set camera position to map center
        camera.position.set(mapCenterX, mapCenterY, 0f)
        camera.update()
    }

    private fun constrainCameraToMap() {
        // Calculate the camera boundaries
        val cameraHalfWidth = camera.viewportWidth * 0.5f
        val cameraHalfHeight = camera.viewportHeight * 0.5f
        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapWidth - cameraHalfWidth)
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapHeight - cameraHalfHeight)
    }
}
