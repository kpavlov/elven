package me.kpavlov.elven.utils

object Secrets {
    private val values = mutableMapOf<String, String>()

    fun get(key: String): String = values[key]!!

    fun put(
        key: String,
        value: String,
    ) {
        values[key] = value
    }
}
