package com.example.dailylife.ui.screen.account

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.dailylife.R
import com.example.dailylife.navigation.DailyLifeScreen
import com.example.dailylife.state.AccountState
import com.example.dailylife.util.convertString
import com.example.dailylife.util.headerModifier
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.viewmodel.AccountViewModel
import java.time.YearMonth

@Composable
fun AccountScreen(
    navController: NavController,
    accountViewModel: AccountViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.ivory))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val currentYM by accountViewModel.currentYM.collectAsStateWithLifecycle()

            AccountTopBarArea(
                ym = currentYM,
                increaseMonth = { accountViewModel.increaseCurrentYM() },
                decreaseMonth = { accountViewModel.decreaseCurrentYM() }
            )

            HorizontalDivider()

            AccountInfoArea()

            HorizontalDivider()

            AccountContentArea(
                modifier = Modifier.weight(1f),
                accountViewModel = accountViewModel
            )
        }

        AccountAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 10.dp),
            onClick = {
                navController.navigate(DailyLifeScreen.AddAccount.name) {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
private fun AccountTopBarArea(
    ym: YearMonth,
    increaseMonth: () -> Unit,
    decreaseMonth: () -> Unit
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
                modifier = Modifier.roundRippleClickable(
                    rippleColor = colorResource(R.color.black),
                    onClick = decreaseMonth
                )
            )

            Text(
                text = ym.convertString(),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )

            Icon(
                painter = painterResource(R.drawable.next),
                contentDescription = null,
                modifier = Modifier.roundRippleClickable(
                    rippleColor = colorResource(R.color.black),
                    onClick = increaseMonth
                )
            )
        }
    }
}

@Composable
private fun AccountInfoArea() {
    // 임시
    val consumptionInfo = arrayOf(AccountState.INCOME to 0, AccountState.EXPEND to 0, AccountState.TOTAL to 0)

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
    consumption: Pair<AccountState, Int>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when(consumption.first) {
                AccountState.INCOME -> stringResource(R.string.income)
                AccountState.EXPEND -> stringResource(R.string.expend)
                AccountState.TOTAL -> stringResource(R.string.total)
            },
            fontSize = 15.sp
        )

        Text(
            text = consumption.second.toString(),
            color = when(consumption.first) {
                AccountState.INCOME -> colorResource(R.color.medium_blue)
                AccountState.EXPEND -> colorResource(R.color.red)
                AccountState.TOTAL -> colorResource(R.color.black)
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
    modifier: Modifier,
    accountViewModel: AccountViewModel
) {
    Box(
        modifier = modifier
    ) {
        val currentYMInfoList by accountViewModel.currentYMAccountList.collectAsStateWithLifecycle()

        if(currentYMInfoList.isEmpty()) {
            BlankArea()
        } else {
            AccountBundle()
        }
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

@Composable
private fun AccountBundle() {

}

@Composable
private fun AccountItemHeader(
    day: Int,
    dayOfWeek: String,
    totalIncome: Long,
    totalExpend: Long,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.headerModifier(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            Text(
                text = day.toString(),
                fontWeight = FontWeight.SemiBold
            )

            Box(
                modifier = Modifier.background(
                    color = colorResource(R.color.gray_asparagus),
                    shape = RoundedCornerShape(16.dp)
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayOfWeek,
                    fontSize = 12.sp
                )
            }
        }

        if(isExpanded) {
            content()
        }
    }
}