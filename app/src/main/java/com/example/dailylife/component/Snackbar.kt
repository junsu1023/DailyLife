package com.example.dailylife.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun TodoSnackBar(
    modifier: Modifier,
    message: String,
    changedState: () -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SnackbarHost(
        modifier = modifier,
        hostState = snackbarState
    )

    LaunchedEffect(Unit) {
        scope.launch {
            snackbarState.showSnackbar(
                message = message,
                actionLabel = null,
                duration = SnackbarDuration.Short
            )

            changedState()
        }
    }
}