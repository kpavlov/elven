package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

object AudioController {
    private val music: Music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/background.wav"))

    init {
        music.volume = 0.5f // sets the volume to half the maximum volume
        music.isLooping = true
    }

    fun playMusic() {
        if (!music.isPlaying) {
            music.play()
        }
    }

    fun stopMusic() {
        if (music.isPlaying) {
            music.stop()
        }
    }

    fun dispose() {
        music.dispose()
    }
}
