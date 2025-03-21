package me.kpavlov.elven

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.scene2d.KDialog
import ktx.scene2d.KVerticalGroup
import ktx.scene2d.KWindow
import ktx.scene2d.actors
import ktx.scene2d.dialog
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.textArea
import ktx.scene2d.verticalGroup
import ktx.scene2d.window

object ChatController {
    private lateinit var chatMessages: KVerticalGroup
    private lateinit var chatWindow: KWindow
    private lateinit var chatDialog: KDialog
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
        initDialog()
    }

    private fun initDialog() {
        stage.actors {
            chatDialog =
                dialog(title = "Hee") {
                    verticalGroup {
                        textArea(text = "This is a text")
                    }
                    button("Cancel", "Cancel")
                    button("OK", "OK")
                }
            chatDialog.isVisible = false
            chatDialog.isModal = true
        }
    }

    private fun initChatWindow() {
        stage.actors {
            chatWindow =
                window("Chat") {
                    image { }
//                    scrollPane {
                    chatMessages = verticalGroup()
//                    }
                }
//            chatWindow.width = 300f
//            chatWindow.height = 150f
            chatWindow.setKeepWithinStage(true)
            chatWindow.setPosition(
                stage.width - chatWindow.width,
                stage.height - chatWindow.height,
            )
            chatWindow.isVisible = false
        }
    }

    fun say(
        actor: Actor,
        message: String,
    ) {
        chatWindow.titleLabel.setText(actor.name)
        chatWindow.setPosition(actor.x + actor.width, actor.y + actor.height)
        chatMessages.clearChildren()
        chatMessages.label(message)
        chatWindow.pack()
        chatWindow.toFront()
        chatWindow.isVisible = true
    }

    fun hideChatWindow() {
        chatWindow.isVisible = false
    }
}
