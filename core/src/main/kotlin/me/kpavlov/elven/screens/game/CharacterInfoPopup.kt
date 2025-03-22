package me.kpavlov.elven.screens.game

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.actors.onKeyUp
import ktx.scene2d.KVerticalGroup
import ktx.scene2d.KWindow
import ktx.scene2d.actors
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.verticalGroup
import ktx.scene2d.window
import me.kpavlov.elven.characters.AbstractCharacter

object CharacterInfoPopup {
    private lateinit var chatMessages: KVerticalGroup
    private lateinit var chatWindow: KWindow
    private lateinit var font: BitmapFont

    private lateinit var stage: Stage

    fun attachToStage(
        stage: Stage,
        font: BitmapFont,
    ) {
        this.stage = stage
        this.font = font
        initActors()
    }

    private fun initActors() {
        initChatWindow()
    }

    private fun initChatWindow() {
        stage.actors {
            chatWindow =
                window("Chat") {
                    image { }
                    chatMessages = verticalGroup()
                    onKeyUp { keyCode ->
                        if (keyCode == Keys.ESCAPE) {
                            isVisible = false
                            true
                        } else {
                            false
                        }
                    }
                }
            chatWindow.setKeepWithinStage(true)
            chatWindow.setPosition(
                stage.width - chatWindow.width,
                stage.height - chatWindow.height,
            )
            chatWindow.isVisible = false
        }
    }

    fun showInfo(actor: AbstractCharacter) {
        val greeting =
            buildString {
                append("I am ${actor.name}")
                if (actor.coins > 0) {
                    append(". I have ${actor.coins} coins 🤑")
                }
            }
        chatWindow.titleLabel.setText(actor.name)
        chatWindow.setPosition(actor.x + actor.width, actor.y + actor.height)
        chatMessages.clearChildren()
        chatMessages.label(greeting)
        chatWindow.pack()
        chatWindow.toFront()
        chatWindow.isVisible = true
    }
}
