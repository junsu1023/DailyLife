package com.example.dailylife.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.dailylife.R

@Composable
fun HorizontalDivider() {
    HorizontalDivider(
        color = colorResource(R.color.platinum),
        thickness = 1.dp
    )
}