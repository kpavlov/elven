package me.kpavlov.elven.utils

object Secrets {
    private val values = mutableMapOf<String, String>()

    fun get(key: String): String = values.get(key)!!

    fun put(
        key: String,
        value: String,
    ) {
        values[key] = value
    }
}
