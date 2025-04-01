package com.example.dailylife.util

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.dailylife.R

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

@Composable
fun Modifier.headerModifier(): Modifier = composed {
    Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 10.dp)
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
}

@Composable
fun Modifier.topBarModifier(): Modifier = composed {
    Modifier
        .fillMaxWidth()
        .height(50.dp)
        .background(colorResource(R.color.bone))
}