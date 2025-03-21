package me.kpavlov.elven.ai

import dev.langchain4j.agent.tool.Tool
import ktx.log.logger

private val logger = logger<WeatherTools>()

@Suppress("unused")
object WeatherTools {
    @Tool("Returns current sea temperature, in Celsius")
    fun seaTemperature(): Int {
        logger.info { "🌡️ Measuring sea temperature" }
        return IntRange(15, 30).random()
    }

    @Tool("Returns current surface air temperature, in Celsius")
    fun surfaceAirTemperature(): Int {
        logger.info { "🌡️ Measuring surface air temperature" }
        return IntRange(12, 40).random()
    }
}
