package me.kpavlov.elven

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile

/**
 * Utility class for map-related operations.
 */
object MapUtils {
    /**
     * Checks if a tile at the given world coordinates is water.
     *
     * @param map The tiled map
     * @param worldX The x-coordinate in world space
     * @param worldY The y-coordinate in world space
     * @return true if the tile is water, false otherwise
     */
    fun isWaterTile(
        map: TiledMap,
        worldX: Float,
        worldY: Float,
    ): Boolean {
        // Get the ground layer (index 0)
        val groundLayer = map.layers[0] as? TiledMapTileLayer ?: return false

        // Get tile width and height
        val tileWidth = map.properties["tilewidth", Int::class.java].toFloat()
        val tileHeight = map.properties["tileheight", Int::class.java].toFloat()

        // Convert world coordinates to tile coordinates
        val tileX = (worldX / tileWidth).toInt()
        val tileY = (worldY / tileHeight).toInt()

        // Check if the coordinates are within the map bounds
        if (tileX < 0 || tileX >= groundLayer.width || tileY < 0 || tileY >= groundLayer.height) {
            return true // Treat out-of-bounds as water to prevent moving off the map
        }

        // Get the cell at the tile coordinates
        val cell = groundLayer.getCell(tileX, tileY) ?: return false

        // Get the tile ID
        val tile = cell.tile
        val animated = tile is AnimatedTiledMapTile
        cell.tile.id

        // Check if the tile ID is in the set of water tile IDs
        return animated // waterTileIds.contains(tileId)
    }

    fun currentObject(
        map: TiledMap,
        worldX: Float,
        worldY: Float,
    ): String? {
        // Get the ground layer (index 0)
        val objectsLayer = map.layers.find { it.name == "Objects" } as? TiledMapTileLayer ?: return null

        // Get tile width and height
        val tileWidth = map.properties["tilewidth", Int::class.java].toFloat()
        val tileHeight = map.properties["tileheight", Int::class.java].toFloat()

        // Convert world coordinates to tile coordinates
        val tileX = (worldX / tileWidth).toInt()
        val tileY = (worldY / tileHeight).toInt()

        // Check if the coordinates are within the map bounds
        if (tileX < 0 || tileX >= objectsLayer.width || tileY < 0 || tileY >= objectsLayer.height) {
            return null // Treat out-of-bounds as water to prevent moving off the map
        }

        // Get the cell at the tile coordinates
        val cell = objectsLayer.getCell(tileX, tileY) ?: return null

        // Get the tile ID
        cell.tile

        // Check if the tile ID is in the set of water tile IDs
        return null
    }
}
