package com.example.dailylife.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dailylife.R
import com.example.dailylife.util.roundRippleClickable

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckChanged: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .roundRippleClickable(
                rippleColor = colorResource(R.color.black),
                onClick = { onCheckChanged(!checked) }
            )
    ) {
        Icon(
            painter = if(checked) painterResource(R.drawable.checkbox_selected) else painterResource(R.drawable.checkbox_unselected),
            contentDescription = null
        )
    }
}