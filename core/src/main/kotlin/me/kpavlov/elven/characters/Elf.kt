package me.kpavlov.elven.characters

import com.badlogic.gdx.graphics.Texture
import me.kpavlov.elven.arrowControls

class Elf :
    PlayerCharacter(
        name = "Eldrin",
        texture = Texture("characters/elf.png"),
        speed = .01f,
        width = 0.7f,
        height = 0.7f,
        controls = arrowControls,
    )
