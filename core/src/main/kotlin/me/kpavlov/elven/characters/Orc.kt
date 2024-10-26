package me.kpavlov.elven.characters

import com.badlogic.gdx.graphics.Texture

class Orc :
    AbstractCharacter(
        name = "Grumz",
        texture = Texture("characters/orc-1.png"),
        speed = .01f,
        width = 0.5f,
        height = 0.7f,
    )
