package com.example.dailylife.util

interface MultiClickEventCutter {
    fun processEvent(event: () -> Unit)

    companion object
}

object MultiClickEventCutterImpl: MultiClickEventCutter {
    private val now: Long get() = System.currentTimeMillis()
    private var lastClickTimeMs = 0L

    override fun processEvent(event: () -> Unit) {
        if(now - lastClickTimeMs >= 200L) {
            event()
            lastClickTimeMs = now
        }
    }
}

internal fun MultiClickEventCutter.Companion.get(): MultiClickEventCutter = MultiClickEventCutterImpl

