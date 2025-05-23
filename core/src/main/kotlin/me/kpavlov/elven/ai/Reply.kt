package me.kpavlov.elven.ai

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import dev.langchain4j.model.output.structured.Description

/**
 * Represents a reply provided by an AI Character in response to a user message or interaction.
 *
 * @property text The content of the reply as a String.
 * @property coins The number of coins the AI Character gives to the Player. Defaults to 0.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Reply(
    @param:JsonProperty("text")
        @Description("Text response from the AI Character")
        val text: String = "",
    @param:JsonProperty("coins", defaultValue = "0")
        @Description(
            "Number of coins the AI Character gives to the Player when positive, " +
                "or takes from the Player when negative. " +
                "If no coins are given or taken then 0",
        )
        val coins: Int = 0,
    )
