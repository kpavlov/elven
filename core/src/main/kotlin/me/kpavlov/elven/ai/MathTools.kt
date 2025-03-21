package me.kpavlov.elven.ai

import dev.langchain4j.agent.tool.Tool
import ktx.log.logger

private val logger = logger<MathTools>()

class MathTools {
    @Tool("Returns sum of two integers")
    fun add(
        a: Int,
        b: Int,
    ): Int {
        logger.info { "🧮 Adding $a and $b" }
        return a + b
    }

    @Tool("Returns the result of multiplication of two integers")
    fun multiply(
        a: Int,
        b: Int,
    ): Int {
        logger.info { "🧮 Multiplying $a and $b" }
        return a * b
    }
}
