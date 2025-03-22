package me.kpavlov.elven.utils

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

class StaticOrthogonalTiledMapRenderer(
    map: TiledMap,
    unitScale: Float,
) : OrthogonalTiledMapRenderer(map, unitScale) {
    override fun renderTileLayer(layer: com.badlogic.gdx.maps.tiled.TiledMapTileLayer) {
        beginRender()
        val width = layer.width
        val height = layer.height
        val tileWidth = layer.tileWidth * unitScale
        val tileHeight = layer.tileHeight * unitScale

        // iterate and render tiles statically
        for (y in 0 until height) {
            for (x in 0 until width) {
                val cell = layer.getCell(x, y) ?: continue
                val tile = cell.tile ?: continue

                val flipX = cell.flipHorizontally
                cell.flipVertically
                val rotation = cell.rotation

                // draw tile statically, forced first frame
                val region = tile.textureRegion
                val x1 = x * tileWidth
                val y1 = y * tileHeight

                batch.draw(
                    region,
                    x1,
                    y1,
                    tileWidth / 2,
                    tileHeight / 2,
                    tileWidth,
                    tileHeight,
                    1f,
                    1f,
                    rotation.toFloat(),
                    flipX,
                )
            }
        }
        endRender()
    }
}
