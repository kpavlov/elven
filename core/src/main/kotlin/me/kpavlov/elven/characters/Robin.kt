package me.kpavlov.elven.characters

import me.kpavlov.elven.arrowControls

class Robin :
    PlayerCharacter(
        name = "Robin",
        speed = 1,
        width = 64,
        height = 64,
        controls = arrowControls,
    )
