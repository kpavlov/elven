package me.kpavlov.elven.characters

import com.badlogic.gdx.graphics.Texture
import me.kpavlov.elven.asdwControls

class Dwarf :
    PlayerCharacter(
        name = "Dawlin",
        texture = Texture("characters/dwarf.png"),
        speed = .01f,
        width = 0.7f,
        height = 0.7f,
        controls = asdwControls,
    )
