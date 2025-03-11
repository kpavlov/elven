package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

object AudioManager {
    //    private val music: Music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/background.wav"))
    private val music: Music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/beach-party-240790.mp3"))

    var musicOn: Boolean = true
    var soundsOn: Boolean = true

    init {
        music.volume = GameConfig.Audio.MUSIC_VOLUME // sets the volume to half the maximum volume
        music.isLooping = true
    }

    fun playMusic() {
        if (musicOn && !music.isPlaying) {
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

    fun playSound(sound: Sound?) {
        if (!soundsOn) {
            return
        }
        sound?.play(GameConfig.Audio.SOUND_VOLUME)
    }
}
