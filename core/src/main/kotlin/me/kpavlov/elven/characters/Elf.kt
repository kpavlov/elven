package me.kpavlov.elven.characters

import com.badlogic.gdx.graphics.Texture
import me.kpavlov.elven.arrowControls

class Elf :
    PlayerCharacter(
        name = "Eldrin",
        texture = Texture("characters/elf.png"),
        speed = 1,
        width = 64,
        height = 64,
        controls = arrowControls,
    )
