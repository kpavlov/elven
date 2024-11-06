package me.kpavlov.elven.utils

import com.badlogic.gdx.utils.Disposable

fun <T> Disposable.use(block: (Disposable) -> T): T {
    try {
        return block(this)
    } finally {
        dispose()
    }
}
