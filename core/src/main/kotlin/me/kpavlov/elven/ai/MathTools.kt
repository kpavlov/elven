package me.kpavlov.elven.ai

import dev.langchain4j.agent.tool.Tool

class MathTools {
    @Tool("Returns sum of two integers")
    fun add(
        a: Int,
        b: Int,
    ): Int = a + b

    @Tool("Returns the result of multiplication of two integers")
    fun multiply(
        a: Int,
        b: Int,
    ): Int = a * b
}
