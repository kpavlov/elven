package me.kpavlov.elven

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.actors.stage
import ktx.scene2d.actor
import ktx.scene2d.actors
import me.kpavlov.elven.characters.*

private val tileWidth = 7f // The width of one tile
private val tileHeight = 7f // The height of one tile

private val input = Gdx.input

/**
 * [com.badlogic.gdx.ApplicationListener] implementation shared by all
 * platforms.
 */
class Main : ApplicationAdapter() {
    private lateinit var batch: Batch
    private lateinit var elf: Elf
    private lateinit var dwarf: Dwarf
    private lateinit var orc: Orc
    private lateinit var viewport: Viewport
    private lateinit var touchPos: Vector2
    private lateinit var map: TiledMap
    private lateinit var camera: OrthographicCamera
    private lateinit var cameraController: CameraController
    private lateinit var mapRenderer: IsometricTiledMapRenderer
    private lateinit var stage: Stage

    private val allCharacters = mutableListOf<AbstractCharacter>()
    private val playerCharacters = mutableListOf<PlayerCharacter>()

    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        map = TmxMapLoader().load("scenes/grass_and_water/isometric_grass_and_water.tmx")
        mapRenderer = IsometricTiledMapRenderer(map)

        // Set up the Stage and UI
        stage = stage(viewport = ScreenViewport(camera))
        Gdx.input.inputProcessor = stage // Make the stage receive input
        stage.isDebugAll = true

        batch = stage.batch
        viewport = stage.viewport

        cameraController = CameraController(camera, input)

        positionCameraAtMapCenter(camera, map)

        elf = Elf()
        elf.zIndex = 10
        elf.setPosition(Gdx.graphics.width.toFloat() / 2 - 1, Gdx.graphics.height.toFloat() / 2 - 1)
        dwarf = Dwarf()
        orc = Orc()

        allCharacters.add(orc)

        // viewport = FitViewport(10f, 10f)

        touchPos = Vector2()

//        cameraController.actor = elf

        orc.moveTo(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.height.toFloat() / 2)
        elf.moveTo(300, 200)
        dwarf.moveTo(30, 150)

        // Add UI elements to the stage (optional)
        val image = Image(Texture("terrains/forest.png"))
        image.setPosition(100f, 100f) // Positioning the image
        image.width = 100f
        image.height = 100f

        stage.addActor(image)

        stage.actors {
            actor(orc) {
                allCharacters += orc
            }
            actor(elf) {
                playerCharacters += elf
            }
            actor(dwarf) {
                playerCharacters += dwarf
            }
        }
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        stage.viewport.update(width, height, true)
    }

    override fun render() {
        // organize code into three methods
        input()
        logic()
        draw()
    }

    private fun input() {
        playerCharacters.forEach {
            it.reactOnControls(input)
        }

        cameraController.reactOnControls()
    }

    private fun logic() {
        // to implement
    }

    private fun draw() {
        ScreenUtils.clear(MOSSY_GREEN)

        // Update camera and set the view for the map renderer
        camera.update()
        mapRenderer.setView(camera)

        // Render the isometric map
        mapRenderer.render()

        // Update and render the stage
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun pause() {
    }

    override fun dispose() {
        elf.dispose()
        map.dispose()
        mapRenderer.dispose()
        stage.dispose()
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
        val mapCenterX = (mapWidthInTiles + mapHeightInTiles) * (tileWidth / 2) / 2
        val mapCenterY = (mapHeightInTiles - mapWidthInTiles) * (tileHeight / 2) / 2

        // Set camera position to map center
        camera.position.set(mapCenterX, mapCenterY, 0f)
        camera.update()
    }
}
