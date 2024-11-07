package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors
import ktx.scene2d.table

class SettingsScreen(
    private val stage: Stage,
    skin: Skin = Scene2DSkin.defaultSkin,
) {
    private val preferences: Preferences = Gdx.app.getPreferences("GameSettings")

    init {
        val soundCheckBox =
            CheckBox("Sound", skin).apply {
                isChecked = preferences.getBoolean("soundEnabled", true)
                AudioManager.soundsOn = isChecked
                addListener {
                    preferences.putBoolean("soundEnabled", isChecked)
                    preferences.flush()
                    AudioManager.soundsOn = isChecked
                    true
                }
            }

        val musicCheckBox =
            CheckBox("Music", skin).apply {
                isChecked = preferences.getBoolean("musicEnabled", true)
                if (isChecked) {
                    AudioManager.playMusic()
                }
                addListener {
                    preferences.putBoolean("musicEnabled", isChecked)
                    preferences.flush()
                    AudioManager.musicOn = isChecked
                    if (isChecked) {
                        AudioManager.playMusic()
                    } else {
                        AudioManager.stopMusic()
                    }
                    true
                }
            }

        stage.actors {
            table {
                defaults().pad(PAD_SIZE)
                add(soundCheckBox)
                add(musicCheckBox)
            }.setPosition(80f, 120f)
        }
    }
}
