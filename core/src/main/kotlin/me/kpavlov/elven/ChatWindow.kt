package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.onKeyDown
import ktx.scene2d.KDialog
import ktx.scene2d.KVerticalGroup
import ktx.scene2d.actors
import ktx.scene2d.dialog
import ktx.scene2d.horizontalGroup
import ktx.scene2d.label
import ktx.scene2d.scrollPane
import ktx.scene2d.table
import ktx.scene2d.textArea
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import me.kpavlov.elven.characters.AbstractCharacter
import me.kpavlov.elven.characters.AiCharacter
import me.kpavlov.elven.characters.PlayerCharacter

private const val PAD_SIZE = 10f

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
                    table {
                        scrollPane {
                            fadeScrollBars = false
                            chatMessages =
                                verticalGroup {
                                    fill()
                                }
                        }.cell(grow = true)
                        row()

                        inputTextArea =
                            textArea("") {
                                setPrefRows(2f)
                                onKeyDown {
                                    if (it == Keys.ENTER) {
                                        handleInputMessage()
                                    }
                                }
                            }.cell(growX = true)
                        row()

                        horizontalGroup {
                            textButton("Leave") {
                                onClick { leaveChat() }
                            }
                            grow()

                            textButton("Say") {
                                onClick { handleInputMessage() }
                                align(Align.right)
                            }
                        }.pad(PAD_SIZE)
                    }.cell(grow = false, maxWidth = 1000f, maxHeight = 400f, minWidth = 600f)
                }
        }

        configureDialog()
    }

    private fun configureDialog() {
        dialog.apply {
            isVisible = false
            isModal = true
            isResizable = true
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
                addMessage(currentPlayer, text)
                inputTextArea.isDisabled = true
                inputTextArea.text = ""

                currentNpc.ask(text, from = currentPlayer) {
                    if (dialog.isVisible) {
                        addMessage(currentNpc, it)
                        inputTextArea.isDisabled = false
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
        dialog.titleLabel.setText("Chat with ${npc.name}")
        dialog.pack()
        dialog.show(stage)
        dialog.toFront()
        dialog.isVisible = true
    }

    fun say(
        actor: PlayerCharacter,
        text: String,
    ) {
        inputTextArea.text = text
        handleInputMessage()
    }

    private fun addMessage(
        actor: AbstractCharacter,
        text: String,
    ) {
        chatMessages.horizontalGroup {
            label("${actor.name}: ")
            label(text) {
            }
            grow()
        }
        actor.logger.info { text }
        dialog.align(Align.bottom)
        dialog.pack()
//        dialog.centerPosition()
        dialog.toFront()
    }
}
