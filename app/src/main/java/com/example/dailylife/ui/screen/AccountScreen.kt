package com.example.dailylife.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailylife.R
import com.example.dailylife.state.ConsumptionState
import com.example.dailylife.util.roundRippleClickable

@Composable
fun AccountScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.ivory))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AccountTopBarArea()

            HorizontalDivider()

            ConsumptionInformationArea()

            HorizontalDivider()

            AccountContentArea(
                modifier = Modifier.weight(1f)
            )
        }


        AccountAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 10.dp),
            onClick = { }
        )
    }
}

@Composable
private fun AccountTopBarArea(
//    ym: YearMonth
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = colorResource(R.color.bone)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.before),
                contentDescription = null,
            )

            Text(
                text = "2025년 3월",
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )

            Icon(
                painter = painterResource(R.drawable.next),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ConsumptionInformationArea() {
    // 금액은 viewmodel에서 관리? 생각해봐야함
    val consumptionInfo = arrayOf(ConsumptionState.INCOME to 0, ConsumptionState.EXPEND to 0, ConsumptionState.TOTAL to 0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        for(consumption in consumptionInfo) {
            InfoArea(
                modifier = Modifier.weight(1f),
                consumption = consumption
            )
        }
    }
}

@Composable
private fun InfoArea(
    modifier: Modifier,
    consumption: Pair<ConsumptionState, Int>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when(consumption.first) {
                ConsumptionState.INCOME -> stringResource(R.string.income)
                ConsumptionState.EXPEND -> stringResource(R.string.expend)
                ConsumptionState.TOTAL -> stringResource(R.string.total)
            },
            fontSize = 15.sp
        )

        Text(
            text = consumption.second.toString(),
            color = when(consumption.first) {
                ConsumptionState.INCOME -> colorResource(R.color.medium_blue)
                ConsumptionState.EXPEND -> colorResource(R.color.red)
                ConsumptionState.TOTAL -> colorResource(R.color.black)
            },
            fontSize = 15.sp
        )
    }
}

@Composable
private fun AccountAddButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(R.drawable.add2),
        contentDescription = null,
        tint = colorResource(R.color.gray_asparagus),
        modifier = modifier
            .size(40.dp)
            .roundRippleClickable(
                rippleColor = colorResource(R.color.gray_asparagus),
                onClick = onClick
            )
    )
}

@Composable
private fun AccountContentArea(
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        BlankArea()
    }
}

@Composable
private fun BlankArea() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.blank_account),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}