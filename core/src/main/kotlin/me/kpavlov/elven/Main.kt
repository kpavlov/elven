package me.kpavlov.elven

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import me.kpavlov.elven.characters.*
import java.util.concurrent.ConcurrentLinkedQueue

private val tileWidth = 7f // The width of one tile
private val tileHeight = 7f // The height of one tile

private val input = Gdx.input

/**
 * [com.badlogic.gdx.ApplicationListener] implementation shared by all
 * platforms.
 */
class Main : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var tileTexture: Texture
    private lateinit var elf: Elf
    private lateinit var dwarf: Dwarf
    private lateinit var orc: Orc
    private lateinit var viewport: FitViewport
    private lateinit var touchPos: Vector2
    private lateinit var map: TiledMap
    private lateinit var camera: OrthographicCamera
    private lateinit var cameraController: CameraController
    private lateinit var mapRenderer: IsometricTiledMapRenderer

    private val allCharacters = ConcurrentLinkedQueue<AbstractCharacter>()
    private val playerCharacters = ConcurrentLinkedQueue<PlayerCharacter>()

    override fun create() {
        batch = SpriteBatch()

        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cameraController = CameraController(camera, input)
//        camera.position.set(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f, 0f)

        map = TmxMapLoader().load("scenes/grass_and_water/isometric_grass_and_water.tmx")
        mapRenderer = IsometricTiledMapRenderer(map, batch)

        tileTexture = Texture("terrains/forest.png")
        elf = Elf()
        dwarf = Dwarf()
        orc = Orc()

        allCharacters.add(orc)

        playerCharacters.add(dwarf)
        playerCharacters.add(elf)

        viewport = FitViewport(10f, 10f)
        touchPos = Vector2()

//        cameraController.actor = elf

        orc.moveTo(1, 3)
        elf.moveTo(3, 2.6)
        dwarf.moveTo(2, 2.6)
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        viewport.update(width, height, true) // true centers the camera
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

        if (Gdx.input.isTouched) {
            // https://libgdx.com/wiki/start/a-simple-game#input-controls
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()) // Get where the touch happened on screen
            viewport.unproject(touchPos) // Convert the units to the world units of the viewport
            println("TouchPos: $touchPos")
            // bucketSprite.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
        }
    }

    private fun logic() {
        // to implement
    }

    private fun draw() {
        ScreenUtils.clear(MOSSY_GREEN)

        val worldWidth = viewport.worldWidth
        val worldHeight = viewport.worldHeight

        // Update camera
        camera.update(true)

        mapRenderer.setView(camera)

        // Render map
        mapRenderer.render()

        batch.begin()
        viewport.apply()
        batch.setProjectionMatrix(viewport.camera.combined)

        allCharacters.forEach {
            it.render(batch)
        }

        playerCharacters.forEach {
            it.render(batch)
        }

        batch.end()
    }

    override fun pause() {
    }

    override fun dispose() {
        batch.dispose()
        tileTexture.dispose()
        elf.dispose()
        map.dispose()
        mapRenderer.dispose()
    }
}
