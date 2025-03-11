# Elven - AI RPG Demo Project Guidelines

## Project Overview

Elven is an AI-powered RPG (Role-Playing Game) demo built with libGDX, libKTX, Kotlin, and
Langchain4J/Langchain4J-Kotlin. The project demonstrates the integration of AI capabilities into a 2D game environment,
creating an interactive and dynamic gaming experience.

### Key Features

- **2D Game World**: Uses libGDX's orthographic camera and tiled maps for rendering a 2D game environment
- **Character System**: Includes both player-controlled and AI-controlled characters
- **Chat System**: Integrated chat functionality for character interactions
- **Audio Management**: Background music and sound effects system
- **Settings Screen**: User-configurable game settings

### Technical Stack

- **Game Framework**: [libGDX](https://libgdx.com/), [linKtx](https://github.com/libktx/ktx)
- **Programming Language**: Kotlin
- **AI Integration
  **: [Langchain4J](https://github.com/langchain4j/langchain4j), [Langchain4j-Kotlin](https://github.com/kpavlov/langchain4j-kotlin)
- **Desktop Platform**: LWJGL3
- **Build System**: Gradle
- **Minimum JDK**: 22+

### Project Structure

- **core**: Main module with the application logic shared by all platforms
- **lwjgl3**: Primary desktop platform implementation
- **assets**: Game resources including maps, sprites, and audio files

### Development Guidelines

1. **Code Style**: Follow Kotlin coding conventions and maintain consistent formatting
2. **Architecture**: Maintain separation of concerns between game logic, rendering, and AI components.
   Follow [SOLID](https://en.wikipedia.org/wiki/SOLID) principles
3. **Performance**: Consider performance implications, especially for AI operations
4. **Documentation**: Document complex algorithms and AI integration points. Prefer self-documenting code
5. **Testing**: Write tests for critical game logic components

### Getting Started

To run the project:

```shell
./gradlew run
```

### Contributing

When contributing to this project:

1. Create a feature branch from main
2. Implement your changes following the project's coding standards
3. Add appropriate tests
4. Submit a pull request with a clear description of your changes

### Resources

- [libGDX Documentation](https://libgdx.com/wiki/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Langchain4J Documentation](https://docs.langchain4j.dev/)
