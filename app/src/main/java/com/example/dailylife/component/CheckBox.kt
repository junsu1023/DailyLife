package com.example.dailylife.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dailylife.R

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckChanged: (Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(checked) }

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .clickable(
                enabled = true,
                onClick = {
                    isChecked = !isChecked
                    onCheckChanged(isChecked)
                }
            )
    ) {
        Icon(
            painter = if(isChecked) painterResource(R.drawable.checkbox_selected) else painterResource(R.drawable.checkbox_unselected),
            contentDescription = null
        )
    }
}