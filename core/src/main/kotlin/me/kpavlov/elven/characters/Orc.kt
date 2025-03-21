package me.kpavlov.elven.characters

import me.kpavlov.elven.ai.Calculator

class Orc :
    AiCharacter(
        name = "Gruk",
        speed = .01f,
        width = 54,
        height = 70,
        coins = 100,
        tools = listOf(Calculator),
    )
