package me.kpavlov.elven.screens.game

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors
import ktx.scene2d.label
import ktx.scene2d.table
import me.kpavlov.elven.GameConfig.PAD_SIZE
import me.kpavlov.elven.characters.PlayerCharacter

class PlayerPanel(
    stage: Stage,
    skin: Skin = Scene2DSkin.defaultSkin,
    player: PlayerCharacter,
) {
    init {
        stage.actors {
            table {
                defaults().pad(PAD_SIZE)
                add(label("Player: ${player.name}"))
            }.setPosition(80f, 10f)
        }
    }
}
