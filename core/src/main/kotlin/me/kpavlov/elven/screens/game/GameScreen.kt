package me.kpavlov.elven.screens.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FillViewport
import ktx.scene2d.StageWidget
import ktx.scene2d.actor
import ktx.scene2d.actors
import me.kpavlov.elven.ChatController
import me.kpavlov.elven.GameConfig
import me.kpavlov.elven.characters.AbstractCharacter
import me.kpavlov.elven.characters.PlayerCharacter

/**
 * Handles the main game screen functionality including rendering, input processing,
 * and game logic.
 */
class GameScreen(
    private val camera: OrthographicCamera,
    private val font: BitmapFont,
) : Disposable {
    private val input = Gdx.input
    private val touchPos = Vector2()
    private var mapWidth: Float
    private var mapHeight: Float
    private lateinit var settingsPanel: SettingsPanel
    private lateinit var mapRenderer: OrthogonalTiledMapRenderer
    private lateinit var stage: Stage
    private val allCharacters = mutableListOf<AbstractCharacter>()
    private val playerCharacters = mutableListOf<PlayerCharacter>()
    private val map: TiledMap = TmxMapLoader().load(GameConfig.MAP_FILE)

    init {

        // Calculate map dimensions
        val tileWidth = map.properties["tilewidth", Int::class.java]
        mapWidth = map.properties["width", Int::class.java].toFloat() * tileWidth
        val tileHeight = map.properties["tileheight", Int::class.java]
        mapHeight = map.properties["height", Int::class.java].toFloat() * tileHeight

        // Initialize map renderer
        mapRenderer = OrthogonalTiledMapRenderer(map, 1f)

        // Set up the Stage and UI
        stage = Stage(FillViewport(mapWidth, mapHeight, camera))
        Gdx.input.inputProcessor = stage

        // Initialize UI components
        settingsPanel = SettingsPanel(stage)
        ChatController.attachToStage(stage, font)
        ChatWindow.attachToStage(stage)
    }

    /**
     * Adds a character to the stage and the specified collection.
     */
    fun <T : AbstractCharacter> StageWidget.addCharacter(
        character: T,
        collection: MutableCollection<T>,
    ) {
        this.actor(character) {
            character.gameMap = map
            collection += character
        }
    }

    /**
     * Adds characters to the game screen.
     */
    fun addCharacters(characters: List<AbstractCharacter>) {
        characters.forEach { character ->
            when (character) {
                is PlayerCharacter -> {
                    stage.actors {
                        addCharacter(character, collection = playerCharacters)
                    }
                }

                else -> {
                    stage.actors {
                        addCharacter(character, collection = allCharacters)
                    }
                }
            }
        }
    }

    /**
     * Handles user input for player characters.
     */
    fun input() {
        playerCharacters.forEach {
            it.reactOnControls(input)
        }
    }

    /**
     * Updates game logic.
     */
    fun logic() {
        playerCharacters.forEach { player ->
            allCharacters.forEach { other ->
                player.checkHit(other)
            }
        }
    }

    /**
     * Renders the game screen.
     */
    fun draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Update camera and set the view for the map renderer
        camera.update()

        // Ensure camera stays within map boundaries
        constrainCameraToMap()

        mapRenderer.setView(camera)

        // Render the map
        mapRenderer.render()

        // Update and render the stage
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    /**
     * Constrains the camera to the map boundaries.
     */
    private fun constrainCameraToMap() {
        // Calculate the camera boundaries
        val cameraHalfWidth = camera.viewportWidth * 0.5f
        val cameraHalfHeight = camera.viewportHeight * 0.5f
        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapWidth - cameraHalfWidth)
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapHeight - cameraHalfHeight)
    }

    /**
     * Resizes the game screen.
     */
    fun resize(
        width: Int,
        height: Int,
    ) {
        stage.viewport.update(width, height, true)
    }

    /**
     * Disposes of resources used by the game screen.
     */
    override fun dispose() {
        playerCharacters.forEach { it.dispose() }
        allCharacters.forEach { it.dispose() }
        mapRenderer.dispose()
        stage.dispose()
    }
}
