package me.kpavlov.elven.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.isOnRenderingThread
import ktx.async.onRenderingThread

fun <T> KtxAsync.ensureOnRenderingThread(block: suspend CoroutineScope.() -> T) =
    {
        if (KtxAsync.isOnRenderingThread()) {
            launch {
                block()
            }
        } else {
            KtxAsync.launch {
                onRenderingThread(block)
            }
        }
    }
