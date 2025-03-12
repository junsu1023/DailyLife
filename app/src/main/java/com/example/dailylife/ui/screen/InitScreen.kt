package com.example.dailylife.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailylife.R

@Composable
fun InitScreen(
    handleClickButton: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alabaster)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            ImageButton(
                resId = R.drawable.todo_icon,
                title = stringResource(R.string.todo_list),
                onClick = { handleClickButton(1) }
            )

            ImageButton(
                resId = R.drawable.calender_icon,
                title = stringResource(R.string.calender),
                onClick = { handleClickButton(2) }
            )
        }

        Spacer(
            modifier = Modifier.height(50.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            ImageButton(
                resId = R.drawable.account_book_icon,
                title = stringResource(R.string.account_book),
                onClick = { handleClickButton(3) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alabaster)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            ImageButton(
                resId = R.drawable.todo_icon,
                title = stringResource(R.string.todo_list),
                onClick = { }
            )

            ImageButton(
                resId = R.drawable.calender_icon,
                title = "Calender",
                onClick = { }
            )
        }

        Spacer(
            modifier = Modifier.height(50.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            ImageButton(
                resId = R.drawable.account_book_icon,
                title = "Account\nBook",
                onClick = { }
            )
        }
    }
}

@Composable
fun ImageButton(
    @DrawableRes resId: Int,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(R.color.bone)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    enabled = true,
                    onClick = onClick
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(resId),
                contentDescription = "Todo"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}