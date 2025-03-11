package me.kpavlov.elven

@Suppress("MagicNumber")
object GameConfig {
    const val DEBUG = false
    const val COLLISION_DISTANCE = 50f
    const val DEFAULT_FONT_SIZE = 16
    const val AVATAR_SIZE = 1f
    const val CHAT_WINDOW_WIDTH = 600f
    const val MAP_FILE = "terrains/island.tmx"
    const val CHARACTER_MOVE_DURATION = 0.4f

    object Audio {
        const val MUSIC_VOLUME = 0.3f
        const val SOUND_VOLUME = 1.0f
    }
}
