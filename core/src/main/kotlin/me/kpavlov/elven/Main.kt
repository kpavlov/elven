package me.kpavlov.elven

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import me.kpavlov.elven.characters.Elf

private val mapWidth = 10
private val mapHeight = 10
private val tileWidth = 7f // The width of one tile
private val tileHeight = 7f // The height of one tile

/**
 * [com.badlogic.gdx.ApplicationListener] implementation shared by all
 * platforms.
 */
class Main : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var tileTexture: Texture
    private lateinit var elf: Elf
    private lateinit var viewport: FitViewport

    override fun create() {
        batch = SpriteBatch()
        tileTexture = Texture("terrains/forest.png")
        elf = Elf()
        viewport = FitViewport(8f, 5f)
        elf.moveTo(3,2.6)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true) // true centers the camera
    }

//    override fun render() {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
//        batch.begin()
//        batch.draw(image, 140f, 210f)
//        batch.end()
//    }

    override fun render() {
        // organize code into three methods
        input()
        logic()
        draw()
    }

    private fun input() {

        elf.run = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            elf.moveEast();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            elf.moveWest();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            elf.moveNorth();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            elf.moveSouth();
        }
    }

    private fun logic() {
    }

    private fun draw() {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
//        ScreenUtils.clear(Color.OLIVE);
        ScreenUtils.clear(MOSSY_GREEN);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        val worldWidth = viewport.worldWidth
        val worldHeight = viewport.worldHeight

        for (y in 0 until worldHeight.toInt()) {
            for (x in 0 until worldWidth.toInt()) {
                // Convert grid coordinates to screen coordinates for isometric projection
                val screenX = (x - y) * (tileWidth / 2)
                val screenY = (x + y) * (tileHeight / 2)

                // Draw the tile at the computed screen position
                batch.draw(tileTexture, screenX, screenY, tileWidth, tileHeight)
            }
        }

//        batch.draw(tileTexture, -5f,-5f)

        elf.render(batch)

        batch.end();
    }


    override fun dispose() {
        batch.dispose()
        tileTexture.dispose()
        elf.dispose()
    }
}
