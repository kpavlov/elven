#  AI RPG Demo

[![Java CI with Gradle](https://github.com/kpavlov/elven/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/kpavlov/elven/actions/workflows/gradle.yml)

![screenshot-1.png](docs/screenshot-1.png)

![screenshot.png](docs/screenshot-2.png)

---
A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

It runs on desktop platform using LWJGL3/Kotlin/Langchain4J

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Prerequisites

Create `.env` file with your OpenAI API key:

```dotenv
OPENAI_API_KEY=sk-proj-...
```

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.

Run it under JDK 22+ with:

```shell
./gradlew run
```
