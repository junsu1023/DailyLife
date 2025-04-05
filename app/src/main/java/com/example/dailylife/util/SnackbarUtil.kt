package com.example.dailylife.util

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun SnackbarHostState.showSnackbarShort(scope: CoroutineScope, message: String) {
    val job = scope.launch {
        this@showSnackbarShort.showSnackbar(message = message)
    }
    delay(1000L)
    job.cancel()
}