package me.kpavlov.elven

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import me.kpavlov.elven.characters.Dwarf
import me.kpavlov.elven.characters.Elf

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
    private lateinit var viewport: FitViewport

    override fun create() {
        batch = SpriteBatch()
        tileTexture = Texture("terrains/forest.png")
        elf = Elf()
        dwarf = Dwarf()
        viewport = FitViewport(8f, 5f)
        elf.moveTo(3,2.6)
        dwarf.moveTo(2,2.6)
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

        elf.run = input.isKeyPressed(Keys.SHIFT_LEFT)

        elf.reactOnControls(input)
        dwarf.reactOnControls(input)
    }

    private fun logic() {
    }

    private fun draw() {
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

        elf.render(batch)
        dwarf.render(batch)

        batch.end();
    }


    override fun dispose() {
        batch.dispose()
        tileTexture.dispose()
        elf.dispose()
    }
}
