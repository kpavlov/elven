package me.kpavlov.elven.characters

import me.kpavlov.elven.ai.WeatherTools

class Crab :
    AiCharacter(
        name = "Alex",
        speed = 1,
        width = 50,
        height = 50,
        tools = listOf(WeatherTools),
    )
