package me.kpavlov.elven.ai

import dev.langchain4j.agent.tool.Tool
import ktx.log.logger

private val logger = logger<Calculator>()

@Suppress("unused")
internal object Calculator {
    @Tool("Calculates the length of a string")
    fun stringLength(s: String): Int {
        logger.info { "🧮 Called stringLength with s='$s'" }
        return s.length
    }

    @Tool("Calculates the sum of two numbers")
    fun add(
        a: Double,
        b: Double,
    ): Double {
        logger.info { "🧮 Called add with a=$a, b=$b" }
        return a + b
    }

    @Tool("Calculates the square root of a number")
    fun sqrt(x: Int): Double {
        logger.info { "🧮 Called sqrt with x=$x" }
        return kotlin.math.sqrt(x.toDouble())
    }

    @Tool("Returns the result of multiplication of two integers")
    fun multiply(
        a: Double,
        b: Double,
    ): Double {
        logger.info { "🧮 Multiplying $a and $b" }
        return a * b
    }
}
