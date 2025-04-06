package com.example.dailylife.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailylife.R
import com.example.dailylife.util.roundRippleClickable

@Composable
fun AccountSelectorBox(
    modifier: Modifier,
    title: String,
    onClose: () -> Unit
) {
    Column(
        modifier = modifier
            .background(colorResource(R.color.white))
    ) {
        HeaderArea(
            title = title,
            onClose = onClose
        )


        SelectorBodyArea(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.gray))
        )
    }
}

@Composable
fun HeaderArea(
    title: String,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(colorResource(R.color.forest_green)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(R.color.white)
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.edit_icon),
            contentDescription = null,
            tint = colorResource(R.color.white),
            modifier = Modifier
                .roundRippleClickable(
                    rippleColor = colorResource(R.color.white),
                    onClick = onClose
                )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painter = painterResource(R.drawable.cancel_icon),
            contentDescription = null,
            tint = colorResource(R.color.white),
            modifier = Modifier
                .roundRippleClickable(
                    rippleColor = colorResource(R.color.white),
                    onClick = onClose
                )
        )

        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun SelectorBodyArea(
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {

    }
}