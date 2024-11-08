package me.kpavlov.elven.ai

import dev.langchain4j.agent.tool.Tool

internal class Calculator {
    @Tool("Calculates the length of a string")
    fun stringLength(s: String): Int {
        println("Called stringLength with s='$s'")
        return s.length
    }

    @Tool("Calculates the sum of two numbers")
    fun add(
        a: Int,
        b: Int,
    ): Int {
        println("Called add with a=$a, b=$b")
        return a + b
    }

    @Tool("Calculates the square root of a number")
    fun sqrt(x: Int): Double {
        println("Called sqrt with x=$x")
        return kotlin.math.sqrt(x.toDouble())
    }
}
