package me.kpavlov.elven.screens.game

import com.badlogic.gdx.graphics.Color
import ktx.scene2d.KWidget
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.label
import me.kpavlov.elven.NPC_COLOR
import me.kpavlov.elven.PLAYER_COLOR
import me.kpavlov.elven.characters.AbstractCharacter
import me.kpavlov.elven.characters.AiCharacter

fun <S> KWidget<S>.characterAvatar(
    actor: AbstractCharacter,
    npcMessageColor: Color = NPC_COLOR,
    playerMessageColor: Color = PLAYER_COLOR,
) = horizontalGroup {
    align(com.badlogic.gdx.utils.Align.topLeft)

    image(actor.avatar) {
        setScaling(com.badlogic.gdx.utils.Scaling.contain)
        setScale(0.5f)
    }

    label(actor.name) {
        // Set color based on sender
        val isNpc = actor is AiCharacter
        color = if (isNpc) npcMessageColor else playerMessageColor
    }
}
