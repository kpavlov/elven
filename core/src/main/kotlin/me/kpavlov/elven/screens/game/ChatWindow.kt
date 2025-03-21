package me.kpavlov.elven.screens.game

/**
 * ChatWindow - A user interface component for in-game character conversations
 *
 * UX Improvements implemented:
 * 1. Visual distinction between player and NPC messages through colors and alignment
 * 2. Improved spacing and padding for better readability
 * 3. Thinking indicator to show when an NPC is responding
 * 4. Better positioning and sizing of the chat window
 * 5. Enhanced visual hierarchy with font scaling and colors
 * 6. Improved handling of special messages (coins)
 * 7. Better keyboard interaction (Shift+Enter for newlines)
 * 8. Consistent centering of the dialog on screen
 */

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
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
import ktx.scene2d.label
import ktx.scene2d.scrollPane
import ktx.scene2d.table
import ktx.scene2d.textArea
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import me.kpavlov.elven.GameConfig.CHAT_WINDOW_WIDTH
import me.kpavlov.elven.GameConfig.PAD_SIZE
import me.kpavlov.elven.GameConfig.SPACING
import me.kpavlov.elven.ai.ChatMessage
import me.kpavlov.elven.characters.AbstractCharacter
import me.kpavlov.elven.characters.AiCharacter
import me.kpavlov.elven.characters.PlayerCharacter
import kotlin.math.abs

object ChatWindow {
    private lateinit var stage: Stage
    private lateinit var chatMessages: KVerticalGroup
    private lateinit var inputTextArea: TextArea
    private lateinit var dialog: KDialog
    private lateinit var scrollPane: ScrollPane
    private lateinit var thinkingIndicator: Label
    private lateinit var player: PlayerCharacter
    private lateinit var npc: AiCharacter

    // Colors for message styling
    private val playerMessageColor = Color(0.2f, 0.6f, 1f, 0.7f) // Light blue
    private val npcMessageColor = Color.CORAL

    private val goldColor = Color.GOLD // Gold color
    private val redColor = Color.RED

    // Constants for improved layout
    private const val CHAT_HEIGHT = 350f
    private const val MESSAGE_PADDING = PAD_SIZE * 2
    private const val AVATAR_SIZE = 48f

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
                    padTop(PAD_SIZE * 2)
                    padBottom(PAD_SIZE * 2)

                    table {
                        // Chat messages scroll pane with improved styling
                        scrollPane =
                            scrollPane {
                                fadeScrollBars = false
                                setOverscroll(false, false)

                                chatMessages =
                                    verticalGroup {
                                        space(SPACING * 1.5f)
                                        pad(SPACING)
                                        grow()
                                    }
                            }.cell(grow = true, height = CHAT_HEIGHT, width = CHAT_WINDOW_WIDTH)
                        row()

                        // Typing indicator
                        thinkingIndicator =
                            label("") {
                                color = Color.LIGHT_GRAY
                                isVisible = false
                            }.cell(
                                growX = true,
                                padLeft = PAD_SIZE,
                                padTop = PAD_SIZE,
                                padBottom = PAD_SIZE,
                                align = Align.left,
                            )
                        row()

                        // Input area with improved styling
                        inputTextArea =
                            textArea("") {
                                setPrefRows(2f)
                                onKeyDown {
                                    if (it == Keys.ENTER &&
                                        !Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) &&
                                        !Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)
                                    ) {
                                        handleInputMessage()
                                    }
                                }
                            }.cell(growX = true, padLeft = PAD_SIZE, padRight = PAD_SIZE)

                        row()

                        // Button row with improved layout
                        horizontalGroup {
                            space(SPACING * 2)

                            textButton("Leave") {
                                onClick { leaveChat() }
                            }.pad(PAD_SIZE * 1.5f)

                            textButton("Say") {
                                onClick { handleInputMessage() }
                            }.pad(PAD_SIZE * 1.5f)
                        }.pad(PAD_SIZE * 1.5f).cell(align = Align.right)
                    }.cell(grow = true, minWidth = CHAT_WINDOW_WIDTH, minHeight = CHAT_HEIGHT + 150f)
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
        player.let { currentPlayer ->
            npc.let { currentNpc ->
                addMessage(currentPlayer, text)
                inputTextArea.isDisabled = true
                inputTextArea.text = ""

                // Show typing indicator
                thinkingIndicator.setText("${currentNpc.name} is thinking...")
                thinkingIndicator.isVisible = true

                currentNpc.ask(text, from = currentPlayer) {
                    if (dialog.isVisible) {
                        // Hide typing indicator
                        thinkingIndicator.isVisible = false

                        addMessage(currentNpc, it.text, it.coins)
                        inputTextArea.isDisabled = false
                        inputTextArea.setKeyboardFocus(true)
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

        // Clear previous chat and reset UI
        chatMessages.clearChildren()
        inputTextArea.text = ""
        inputTextArea.isDisabled = false
        thinkingIndicator.isVisible = false

        // Set dialog title with NPC name
        dialog.titleLabel.setText("Chat with ${npc.name}")

        // Load chat history
        npc.chatHistoryWithPlayer(player).forEach {
            addMessage(it)
        }

        // Position and show dialog
        dialog.pack()
        dialog.layout()
        dialog.setPosition(
            (stage.width - dialog.width) / 2, // Center horizontally
            (stage.height - dialog.height) / 2, // Center vertically
        )
        dialog.show(stage)
        dialog.toFront()
        dialog.isVisible = true

        // Set focus to input field
        inputTextArea.setKeyboardFocus(true)
    }

    fun say(
        actor: PlayerCharacter,
        text: String,
    ) {
        inputTextArea.text = text
        handleInputMessage()
    }

    private fun addMessage(chatMessage: ChatMessage) =
        addMessage(
            actor = chatMessage.from,
            text = chatMessage.text,
            coins = chatMessage.coins,
        )

    private fun addMessage(
        actor: AbstractCharacter,
        text: String,
        coins: Int? = null,
    ) {
        val isNpc = actor is AiCharacter
        val align = if (isNpc) Align.topLeft else Align.topRight

        // Create avatar component
        val avatarComponent =
            AvatarComponent(
                actor = actor,
            )

        // Add message with improved styling
        chatMessages.verticalGroup {
            // Add more space between messages
            space(SPACING * 1.5f)
            pad(PAD_SIZE * 2)
            align(align)

            // Add avatar and name using the avatar component
            avatarComponent.addTo(this)

            // Message text with improved styling
            label(insertLineBreaks(text, 50)) {
                // Shorter line length for better readability
                setAlignment(align)
            }

            // Coins information with improved styling
            if (isNpc && coins != null) {
                if (coins > 0) {
                    label("${actor.name} gave you $coins coin${if (coins > 1) "s" else ""}") {
                        color = goldColor
                    }
                } else if (coins < 0) {
                    label("${actor.name} took away ${abs(coins)} coin${if (coins < -1) "s" else ""}") {
                        color = redColor // Red color
                    }
                }
            }
        }

        // Update layout and scroll to bottom
        chatMessages.pack()
        scrollPane.layout()
        scrollPane.scrollPercentY = 1.0f

        // Log the message
        actor.logger.info { text }

        // Update dialog
        dialog.align(Align.bottom)
        dialog.pack()
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
