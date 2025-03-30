package com.example.dailylife.util

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.roundRippleClickable(
    rippleColor: Color,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = rememberRipple(
        bounded = false,
        color = rippleColor
    )

    clickableSingle(
        interactionSource = interactionSource,
        indication = ripple,
        onClick = onClick
    )
}

@Composable
fun Modifier.noRippleClick(
    onClick: () -> Unit
): Modifier = composed {
    clickableSingle(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

@Composable
fun Modifier.clickableSingle(
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    onClick: () -> Unit
): Modifier = composed {
    val multiClickEventCutter = remember { MultiClickEventCutter.get() }

    Modifier.clickable(
        onClick = { multiClickEventCutter.processEvent { onClick() } },
        interactionSource = interactionSource,
        indication = indication
    )
}