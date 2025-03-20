package com.example.dailylife.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

open class BaseViewModel(): ViewModel() {
    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun CoroutineScope.launchWithLoading(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        ignoreCancellationException: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) = this.launch(
        context, start
    ) {
        _isLoading.value = true
        if(ignoreCancellationException) {
            ignoreCancellationException { block() }
        }
        else {
            block()
        }
        _isLoading.value = false
    }
}

private inline fun <T> ignoreCancellationException(block: () -> T) {
    try {
        block()
    } catch (e: CancellationException) {

    }
}