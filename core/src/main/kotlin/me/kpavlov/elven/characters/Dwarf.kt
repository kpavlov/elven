package me.kpavlov.elven.characters

import me.kpavlov.elven.ai.McpTools

class Dwarf :
    AiCharacter(
        name = "Dawlin",
        speed = 1,
        width = 50,
        height = 50,
        streamingResponses = true,
        toolProvider = McpTools.toolProvider,
    )
