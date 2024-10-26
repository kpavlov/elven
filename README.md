#  Elven Quest: The Mage’s Call

[![Java CI with Gradle](https://github.com/kpavlov/elven/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/kpavlov/elven/actions/workflows/gradle.yml)

## Setting:

A magical, ancient world of towering forests, mystical ruins, and dangerous creatures. The game follows an Elf who embarks on a quest to help a legendary wizard in a great battle against a looming evil force.

## Plot Outline:

### Prologue:

For centuries, the Elves have guarded the sacred forests of Lumenor, a land filled with magic and wonder. However, a growing shadow has emerged from the distant mountains, spreading fear and corruption. A wise and powerful Wizard, known only as The Grey Sage, has sent word to the Elves, asking for aid in a great battle against this darkness.

You, **Eldrin**, a young but skilled Elf warrior, are chosen by your people to find and aid the **Grey Sage** in his battle against the rising evil.

## Story Structure:

### Act 1: The Summons

Introduction: The game begins with Eldrin training in the forest when a raven messenger arrives with a letter from the Grey Sage, requesting urgent help.
First Quest: The player must prepare for the journey by collecting provisions and speaking to village elders. This includes basic combat tutorials.
Journey Begins: Eldrin sets off through the enchanted forests of Lumenor, where the first threats of corrupted creatures begin to emerge.

### Act 2: Crossing the Wilds

Exploration: Eldrin travels across diverse terrains: ancient forests, haunted ruins, and vast plains. The player faces increasing dangers from corrupted beasts and dark forces that are attempting to stop them.
Meet Companions: Along the way, Eldrin encounters a Dwarf warrior and a human ranger, who join the quest to aid the Grey Sage. Each companion has unique abilities and side quests.
Key Event: Eldrin receives a magical relic from a forest spirit, which is essential for combating the dark forces ahead.

### Act 3: The Sage’s Warning

**Arrival at the Sage’s Tower:** Eldrin and the party finally reach the tower where the Grey Sage resides. However, it has been overrun by the enemy forces. The player must fight through the tower, solving puzzles and defeating enemies to reach the Sage.
**Revelation:** The Grey Sage reveals that the dark power is being controlled by an ancient sorcerer named Morgor, who seeks to plunge the world into eternal darkness. The Sage is preparing a ritual to banish Morgor, but he needs time and protection.

### Act 4: The Siege of the Sage’s Tower

Battle Preparation: Eldrin and his allies must fortify the Sage’s tower and prepare for a final siege. This involves gathering magical artifacts, setting traps, and rallying the local forces.
The Siege: Dark creatures attack the tower, and Eldrin must lead the defense, switching between battle tactics and helping the Sage perform his ritual.
Climactic Fight: Morgor himself appears as the final boss, challenging Eldrin and the party in an epic battle that requires all the abilities and powers they’ve gained along the way.

### Epilogue: The Dawn of a New Age

After defeating Morgor, the Grey Sage successfully completes the ritual to banish the darkness. The world begins to heal, and peace returns to the land. Eldrin and his companions are hailed as heroes, but the game leaves room for future adventures as new threats loom on the horizon.

## Gameplay Mechanics:

Isometric View: Players navigate Eldrin through a beautifully rendered isometric world filled with enemies, puzzles, and interactive elements.
Character Progression: As Eldrin levels up, the player can unlock new skills related to archery, swordsmanship, and magic.
Companions: The Dwarf warrior and human ranger provide additional skills and can be commanded to help in battle or assist in puzzles.
Combat: Fast-paced real-time combat with the ability to switch between melee, ranged, and magic attacks.
World Exploration: The game features a semi-open world with various regions to explore, including forests, dungeons, and abandoned fortresses.
Puzzles and Quests: Eldrin must solve environmental puzzles and complete quests given by NPCs to progress in the game and unlock new areas.

## Visual and Audio Design:

Art Style: Hand-painted, detailed environments with a lush fantasy aesthetic. The isometric perspective will enhance the sense of depth, especially in vertical terrains like cliffs, trees, and mountains.
Music: A symphonic soundtrack with themes of wonder, adventure, and danger. Calm forest sounds transition to intense battle music during combat.
Sound Effects: Nature sounds, creature growls, and magical effects to fully immerse the player in the world.

## Side Quests and Exploration:

Ancient Elven Ruins: Eldrin can find old ruins scattered throughout the world, offering lore about the past and valuable treasure.
Companion Quests: Each companion has their own backstory and personal quest that ties into the main story.
Hidden Secrets: Throughout the game, players can find hidden areas with rare items, special enemies, or challenging puzzles.

---
A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
