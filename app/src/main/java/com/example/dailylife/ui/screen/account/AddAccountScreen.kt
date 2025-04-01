package com.example.dailylife.ui.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailylife.R
import com.example.dailylife.component.HorizontalDivider
import com.example.dailylife.state.AccountState
import com.example.dailylife.util.getToday
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.util.topBarModifier
import com.example.dailylife.viewmodel.AccountViewModel

@Composable
fun AddAccountScreen(
    accountViewModel: AccountViewModel
) {
    var accountState by remember { mutableStateOf(AccountState.INCOME) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AddAccountTopBarArea(
            modifier = Modifier.topBarModifier(),
            title = when(accountState) {
                AccountState.INCOME -> stringResource(R.string.income)
                AccountState.EXPEND -> stringResource(R.string.expend)
                else -> stringResource(R.string.error)
            }
        )

        AccountButtonArea(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            accountState = accountState,
            onClick = { accountState = it }
        )

        HorizontalDivider()

        EditInfoArea(
            modifier = Modifier.weight(1f),
            accountState = accountState
        )
    }
}

@Composable
fun AddAccountTopBarArea(
    modifier: Modifier,
    title: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painter = painterResource(R.drawable.back_arrow),
            contentDescription = null
        )

        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun AccountButtonArea(
    modifier: Modifier,
    accountState: AccountState,
    onClick: (AccountState) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(modifier = Modifier.width(10.dp))

        AccountButton(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.income),
            isSelected = accountState == AccountState.INCOME,
            onClick = { onClick(AccountState.INCOME) }
        )

        AccountButton(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.expend),
            isSelected = accountState == AccountState.EXPEND,
            onClick = { onClick(AccountState.EXPEND) }
        )

        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun AccountButton(
    modifier: Modifier,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if(isSelected) colorResource(R.color.white) else colorResource(R.color.platinum)
    val borderColor = when {
        isSelected && title == stringResource(R.string.income) -> colorResource(R.color.medium_blue)
        isSelected && title == stringResource(R.string.expend) -> colorResource(R.color.red)
        else -> colorResource(R.color.dark_gray)
    }
    val textColor = if(isSelected) borderColor else colorResource(R.color.black)

    Box(
        modifier = modifier
            .height(30.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(16.dp),
                color = borderColor
            )
            .roundRippleClickable(
                rippleColor = colorResource(R.color.platinum),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun EditInfoArea(
    modifier: Modifier,
    accountState: AccountState
) {
    Column(
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        EditItem(
            text = stringResource(R.string.date),
            accountState = accountState
        )
    }
}

@Composable
fun EditItem(
    text: String,
    accountState: AccountState
) {
    var curDate by remember { mutableStateOf(getToday()) }
    val focusedIndicatorColor = when(accountState) {
        AccountState.INCOME -> colorResource(R.color.medium_blue)
        AccountState.EXPEND -> colorResource(R.color.red)
        else -> colorResource(R.color.dark_gray)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 15.sp,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        TextField(
            value = curDate,
            onValueChange = { curDate = it },
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = focusedIndicatorColor,
                unfocusedIndicatorColor = colorResource(R.color.gray),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 15.sp,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}