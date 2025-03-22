package me.kpavlov.elven.screens.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.launch
import ktx.actors.onClick
import ktx.actors.onKeyDown
import ktx.actors.onKeyUp
import ktx.actors.setKeyboardFocus
import ktx.async.KtxAsync
import ktx.async.onRenderingThread
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

object ChatDialog {
    private lateinit var stage: Stage
    private lateinit var chatMessages: KVerticalGroup
    private lateinit var inputTextArea: TextArea
    private lateinit var dialog: KDialog
    private lateinit var scrollPane: ScrollPane
    private var currentReplyTextLabel: Label? = null
    private var currentCoinsUpdate: Label? = null
    private lateinit var player: PlayerCharacter
    private lateinit var npc: AiCharacter

    // Colors for message styling
    private val goldColor = Color.GOLD
    private val redColor = Color.RED

    // Constants for improved layout
    private const val CHAT_HEIGHT = 350f
    internal val MESSAGE_PADDING = PAD_SIZE * 2
    private const val INPUT_ROWS = 3f
    private const val BUTTON_PAD_MULTIPLIER = 1.5f

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

                    // Handle Esc key for exit
                    onKeyUp { keyCode ->
                        if (keyCode == Keys.ESCAPE) {
                            leaveChat()
                            true
                        } else {
                            false
                        }
                    }

                    table {
                        // Chat messages scroll pane
                        scrollPane =
                            scrollPane {
                                fadeScrollBars = false
                                setSmoothScrolling(true)
                                setOverscroll(false, false)

                                chatMessages =
                                    verticalGroup {
                                        space(SPACING)
                                        pad(MESSAGE_PADDING)
                                        grow()
                                        expand()
                                    }
                            }.cell(
                                grow = true,
                                height = CHAT_HEIGHT,
                                width = CHAT_WINDOW_WIDTH,
                                fill = true,
                            )
                        row()

                        // Input area
                        inputTextArea =
                            textArea("") {
                                setPrefRows(INPUT_ROWS)
                                onKeyDown {
                                    if (it == Keys.ENTER &&
                                        !Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) &&
                                        !Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)
                                    ) {
                                        handleInputMessage()
                                        true
                                    } else {
                                        false
                                    }
                                }
                            }.cell(growX = true, padLeft = PAD_SIZE, padRight = PAD_SIZE)
                        row()

                        // Button row
                        horizontalGroup {
                            space(SPACING * 2)

                            textButton("Leave") {
                                onClick { leaveChat() }
                            }.pad(PAD_SIZE * BUTTON_PAD_MULTIPLIER)

                            textButton("Send") {
                                onClick { handleInputMessage() }
                            }.pad(PAD_SIZE * BUTTON_PAD_MULTIPLIER)
                        }.pad(PAD_SIZE * BUTTON_PAD_MULTIPLIER)
                            .cell(align = Align.right)
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

        addMessage(player, text)
        inputTextArea.isDisabled = true
        inputTextArea.text = ""

        npc.ask(
            question = text,
            from = player,
            onStart = { reply ->
                updateOnRenderingThread {
                    addMessage(npc, reply.text, reply.coins)
                    inputTextArea.isDisabled = true
                }
            },
            onPartialResponse = { partialText ->
                updateOnRenderingThread {
                    // Update message with new symbols
                    updateCurrentMessage(partialText, append = true)
                }
            },
            onCompleteResponse = { reply ->
                updateOnRenderingThread {
                    updateCurrentMessage(reply.text, coins = reply.coins, append = false)
                    inputTextArea.isDisabled = false
                    inputTextArea.setKeyboardFocus(true)
                }
            },
        )
    }

    private fun updateOnRenderingThread(action: () -> Unit) {
        KtxAsync.launch {
            onRenderingThread {
                if (dialog.isVisible) {
                    action()
                }
            }
        }
    }

    private fun leaveChat() {
        Gdx.app.debug("ChatDialog", "Leave button clicked")
        dialog.hide()
    }

    fun startDialog(
        player: PlayerCharacter,
        npc: AiCharacter,
    ) {
        this.player = player
        this.npc = npc

        // Clear previous chat and reset UI
        resetChatUI()

        // Set dialog title with NPC name
        dialog.titleLabel.apply {
            setText(npc.name)
            setAlignment(Align.center)
        }

        // Load chat history
        loadChatHistory()

        // Position and show dialog
        showCenteredDialog()

        // Set focus to input field
        inputTextArea.setKeyboardFocus(true)
    }

    private fun resetChatUI() {
        chatMessages.clearChildren()
        inputTextArea.text = ""
        inputTextArea.isDisabled = false
    }

    private fun loadChatHistory() {
        npc.chatHistoryWithPlayer(player).forEach {
            addMessage(it)
        }
    }

    private fun showCenteredDialog() {
        dialog.pack()
        dialog.layout()
        dialog.setPosition(
            (stage.width - dialog.width) / 2, // Center horizontally
            (stage.height - dialog.height) / 2, // Center vertically
        )
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

    /**
     * Creates a new chat message UI component.
     * This method is optimized for efficient UI creation and configuration.
     *
     * @param actor The character sending the message
     * @param text The message text
     * @param coins Optional coin amount associated with the message
     */
    private fun createChatMessage(
        actor: AbstractCharacter,
        text: String,
        coins: Int?,
    ) {
        val isNpc = actor is AiCharacter

        chatMessages.apply {
            // Add character avatar
            characterAvatar(actor)

            // Create and configure the message text label
            currentReplyTextLabel =
                label(text) {
                    setAlignment(Align.bottomLeft)
                    wrap = true
                    width = parent.width - 10f
                    setFillParent(true)
                }

            // Only create coins label for NPC messages with non-null coins
            currentCoinsUpdate =
                if (isNpc && coins != null) {
                    label("") {
                        // Initialize coin update display
                        setRewardUpdate(this, coins, actor as AiCharacter)
                    }
                } else {
                    null
                }
        }
    }

    private fun addMessage(chatMessage: ChatMessage) =
        addMessage(
            actor = chatMessage.from,
            text = chatMessage.text,
            coins = chatMessage.coins,
        )

    private fun updateCurrentMessage(
        text: String,
        coins: Int? = null,
        append: Boolean,
    ) {
        currentReplyTextLabel?.let {
            // Directly modify the text without creating intermediate strings
            if (append) {
                it.text.append(text)
            } else {
                it.setText(text)
            }

            setRewardUpdate(currentCoinsUpdate, coins, actor = npc)

            // Update layout to ensure text fills width
            updateLayoutAndScroll()
        }
    }

    /**
     * Updates the layout of chat components and scrolls to the bottom.
     * This method is optimized to perform the minimum necessary layout operations.
     */
    private fun updateLayoutAndScroll() {
        // Pack the chat messages to ensure proper sizing
        chatMessages.pack()

        // Update scroll pane layout and scroll to bottom
        scrollPane.layout()
        scrollPane.scrollPercentY = 1f

        // Only pack the dialog if necessary to avoid expensive layout operations
        dialog.invalidate()
    }

    /**
     * Adds a new message to the chat dialog.
     * This method is optimized to minimize UI updates.
     *
     * @param actor The character sending the message
     * @param text The message text
     * @param coins Optional coin amount associated with the message
     */
    private fun addMessage(
        actor: AbstractCharacter,
        text: String,
        coins: Int? = null,
    ) {
        // Create the chat message UI components
        createChatMessage(actor, text, coins)

        // Update layout and scroll to bottom
        updateLayoutAndScroll()

        // Log the message (debug only)
        actor.logger.debug { text }

        // Ensure dialog is properly positioned and focused
        // These operations are relatively lightweight
        dialog.align(Align.bottom)
        dialog.toFront()
        inputTextArea.setKeyboardFocus(true)
    }

    private fun setRewardUpdate(
        label: Label?,
        coins: Int?,
        actor: AiCharacter,
    ) {
        // Early return if label is null or coins is zero
        if (label == null) {
            return
        }
        if (coins == null || coins == 0) {
            label.isVisible = false
            return
        }

        // Determine color based on coin value
        val color: Color = if (coins > 0) goldColor else redColor

        // Create appropriate message text
        val text: String =
            if (coins > 0) {
                "${actor.name} gave you $coins coin${if (coins > 1) "s" else ""}"
            } else {
                "${actor.name} took away ${abs(coins)} coin${if (coins < -1) "s" else ""}"
            }

        // Update the label (no need for null check since we already checked above)
        label.setText(text)
        label.color = color
        label.isVisible = true
    }
}
