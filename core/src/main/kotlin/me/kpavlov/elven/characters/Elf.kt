package me.kpavlov.elven.characters

import me.kpavlov.elven.arrowControls

class Elf :
    PlayerCharacter(
        name = "Eldrin",
        speed = 1,
        width = 64,
        height = 64,
        controls = arrowControls,
    )
