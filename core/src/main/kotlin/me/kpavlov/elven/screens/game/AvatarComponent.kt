package me.kpavlov.elven.screens.game

import com.badlogic.gdx.graphics.Color
import ktx.scene2d.KWidget
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.verticalGroup
import me.kpavlov.elven.characters.AbstractCharacter
import me.kpavlov.elven.characters.AiCharacter

/**
 * AvatarComponent - A reusable UI component that displays a character's avatar and name
 *
 * This component encapsulates the logic for displaying a character's avatar and name
 * with appropriate styling based on whether the character is an NPC or a player.
 */
class AvatarComponent(
    private val actor: AbstractCharacter,
    private val npcMessageColor: Color = Color.CORAL,
    private val playerMessageColor: Color = Color(0.2f, 0.6f, 1f, 0.7f),
) {
    private val isNpc = actor is AiCharacter

    fun <S> addTo(group: KWidget<S>) {
        group.verticalGroup {
            image(actor.avatar) {
                setScaling(com.badlogic.gdx.utils.Scaling.fit)
                setScale(0.7f) // Slightly larger avatar
            }
            label(actor.name) {
                // Set color based on sender
                color = if (isNpc) npcMessageColor else playerMessageColor
                setFontScale(1.1f) // Slightly larger font for name
            }
        }
    }

    /**
     * Returns whether the actor is an NPC
     */
    fun isNpc(): Boolean = isNpc
}
