package me.kpavlov.elven

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.onKeyDown
import ktx.actors.setKeyboardFocus
import ktx.scene2d.KDialog
import ktx.scene2d.KVerticalGroup
import ktx.scene2d.actors
import ktx.scene2d.dialog
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.scrollPane
import ktx.scene2d.table
import ktx.scene2d.textArea
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import me.kpavlov.elven.characters.AbstractCharacter
import me.kpavlov.elven.characters.AiCharacter
import me.kpavlov.elven.characters.PlayerCharacter

const val PAD_SIZE = 5f
const val SPACING = 15f

object ChatWindow {
    private lateinit var stage: Stage
    private lateinit var chatMessages: KVerticalGroup
    private lateinit var inputTextArea: TextArea
    private lateinit var dialog: KDialog
    private lateinit var scrollPane: ScrollPane
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
                    isMovable = true
                    isResizable = false
                    isModal = true
                    isVisible = false
                    table {
                        scrollPane =
                            scrollPane {
                                fadeScrollBars = false
                                chatMessages =
                                    verticalGroup {
//                                        space(SPACING)
                                    }
                            }.cell(grow = true, height = 250f)
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
                            space(SPACING)

                            textButton("Уйти") {
                                onClick { leaveChat() }
                            }.pad(PAD_SIZE)

                            textButton("Сказать") {
                                onClick { handleInputMessage() }
                                align(Align.right)
                            }.pad(PAD_SIZE)
                        }.pad(PAD_SIZE)
                    }.cell(grow = false, maxWidth = 1000f, maxHeight = 400f, minWidth = 600f)
                }
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
        dialog.layout()
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
        val align =
            if (actor is AiCharacter) {
                Align.topLeft
            } else {
                Align.topRight
            }
        chatMessages
            .verticalGroup {
                align(align)
                image(actor.avatar) {
                    setAlign(align)
                }
                label(actor.name) {
                    setAlignment(align)
                }

                label(insertLineBreaks(text)) {
                    setAlignment(align)
                }
            }

        chatMessages.pack()

        scrollPane.layout()
        scrollPane.scrollPercentY = 1.0f

        actor.logger.info { text }
        dialog.align(Align.bottom)
        dialog.pack()
//        dialog.centerPosition()
        dialog.toFront()
        inputTextArea.setKeyboardFocus(true)
    }

    fun insertLineBreaks(
        text: String,
        maxLineLength: Int = 60,
    ): String {
        val words = text.split(" ") // Split text into words by spaces
        val result = StringBuilder()
        var lineLength = 0

        for (word in words) {
            if (lineLength + word.length + 1 > maxLineLength) {
                // If adding the next word exceeds the max line length, insert a newline
                if (result.isNotEmpty()) {
                    result.append("\n")
                }
                lineLength = 0
            } else if (result.isNotEmpty()) {
                // If it's not the first word, add a space before the word
                result.append(" ")
                lineLength += 1
            }
            result.append(word)
            lineLength += word.length
        }
        return result.toString()
    }
}
