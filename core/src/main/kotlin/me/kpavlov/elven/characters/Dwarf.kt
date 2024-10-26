package me.kpavlov.elven.characters

import com.badlogic.gdx.graphics.Texture
import me.kpavlov.elven.asdwControls

class Dwarf :
    PlayerCharacter(
        name = "Dawlin",
        texture = Texture("characters/dwarf.png"),
        speed = 1,
        width = 50,
        height = 50,
        controls = asdwControls,
    )
