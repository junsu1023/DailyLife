package com.example.dailylife.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.dailylife.R

@Composable
fun AccountCommonText(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 15.sp,
    text: String,
    color: Color
) {
    Text(
        text = text,
        style = TextStyle(
            color = color,
            fontSize = fontSize
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun AccountEditText(
    modifier: Modifier,
    text: String,
    focusedIndicatorColor: Color,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(text = text)
        HorizontalDivider(
            color = colorResource(R.color.gray)
        )
    }
}