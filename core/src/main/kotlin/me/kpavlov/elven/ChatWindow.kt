package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.utils.Align
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.actors.onKeyDown
import ktx.scene2d.KDialog
import ktx.scene2d.KVerticalGroup
import ktx.scene2d.actors
import ktx.scene2d.dialog
import ktx.scene2d.horizontalGroup
import ktx.scene2d.label
import ktx.scene2d.scrollPane
import ktx.scene2d.textArea
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import me.kpavlov.elven.characters.AbstractCharacter
import me.kpavlov.elven.characters.AiCharacter
import me.kpavlov.elven.characters.PlayerCharacter

object ChatWindow {
    private lateinit var stage: Stage
    private lateinit var chatMessages: KVerticalGroup
    private lateinit var inputTextArea: TextArea
    private lateinit var dialog: KDialog
    private var player: PlayerCharacter? = null
    private var npc: AiCharacter? = null

    fun attachToStage(stage: Stage) {
        this.stage = stage
        initializeDialog()
    }

    private fun initializeDialog() {
        stage.actors {
            dialog =
                dialog("Chat") {
                    verticalGroup {
                        createChatScrollPane()
                        createInputTextArea()
                        createButtonGroup()
                    }
                }
        }

        configureDialog()
    }

    private fun KVerticalGroup.createChatScrollPane() {
        scrollPane {
            chatMessages = verticalGroup()
        }
    }

    private fun KVerticalGroup.createInputTextArea() {
        inputTextArea =
            textArea("") {
                onKeyDown {
                    if (it == Keys.ENTER) {
                        handleInputMessage()
                    }
                }
            }
    }

    private fun KVerticalGroup.createButtonGroup() {
        horizontalGroup {
            textButton("Leave") {
                onClick { leaveChat() }
            }
            textButton("Say") {
                onClick { handleInputMessage() }
                align(Align.right)
            }
        }
    }

    private fun configureDialog() {
        dialog.apply {
            isVisible = false
            isModal = true
        }
    }

    private fun handleInputMessage() {
        if (!dialog.isVisible) {
            return
        }
        val text = inputTextArea.text.trim()
        if (text.isEmpty()) {
            return
        }
        player?.let { currentPlayer ->
            npc?.let { currentNpc ->
                say(currentPlayer, text)
                inputTextArea.text = ""
                currentNpc.ask(text, from = currentPlayer) {
                    if (dialog.isVisible) {
                        say(currentNpc, it)
                    }
                }
            }
        }
    }

    private fun leaveChat() {
        Gdx.app.log("Button Click", "Leave button clicked!")
        dialog.hide()
    }

    fun startDialog(
        player: PlayerCharacter,
        npc: AiCharacter,
    ) {
        this.player = player
        this.npc = npc
        chatMessages.clearChildren()
        inputTextArea.text = ""
        dialog.show(stage)
        dialog.isVisible = true
    }

    fun say(
        actor: AbstractCharacter,
        text: String,
    ) {
        chatMessages.horizontalGroup {
            label("${actor.name}: ")
            label(text)
        }
        dialog.pack()
        dialog.centerPosition()
        dialog.toFront()
    }
}
