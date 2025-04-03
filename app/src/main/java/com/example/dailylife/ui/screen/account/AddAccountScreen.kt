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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailylife.R
import com.example.dailylife.component.AccountComponentTextField
import com.example.dailylife.component.HorizontalDivider
import com.example.dailylife.state.AccountState
import com.example.dailylife.util.getToday
import com.example.dailylife.util.noRippleClick
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.util.topBarModifier
import com.example.dailylife.viewmodel.AccountViewModel
import com.example.data.entitiy.AccountEntity

@Composable
fun AddAccountScreen(
    navController: NavController,
    accountViewModel: AccountViewModel
) {
    var accountState by remember { mutableStateOf(AccountState.INCOME) }
    val clickBackButton: () -> Unit = {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AddAccountTopBarArea(
            modifier = Modifier.topBarModifier(),
            title = when(accountState) {
                AccountState.INCOME -> stringResource(R.string.income)
                AccountState.EXPEND -> stringResource(R.string.expend)
                else -> stringResource(R.string.error)
            },
            clickBackButton = clickBackButton
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
            accountState = accountState,
            onSave = {
                accountViewModel.addAccountItem(it)
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun AddAccountTopBarArea(
    modifier: Modifier,
    title: String,
    clickBackButton: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painter = painterResource(R.drawable.back_arrow),
            contentDescription = null,
            modifier = Modifier
                .roundRippleClickable(
                    rippleColor = colorResource(R.color.black),
                    onClick = clickBackButton
                )
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
                color = backgroundColor, shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp, shape = RoundedCornerShape(16.dp), color = borderColor
            )
            .roundRippleClickable(
                rippleColor = colorResource(R.color.platinum), onClick = onClick
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
    accountState: AccountState,
    onSave: (AccountEntity) -> Unit
) {
    val infoKind = when(accountState) {
        AccountState.INCOME -> stringResource(R.string.income)
        AccountState.EXPEND -> stringResource(R.string.expend)
        else -> stringResource(R.string.error)
    }
    var infoDate by remember { mutableStateOf("") }
    var infoCost by remember { mutableStateOf("") }
    var infoClassification by remember { mutableStateOf<String?>(null) }
    var infoContent by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        EditItem(
            text = stringResource(R.string.date),
            subText = getToday(),
            accountState = accountState,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onTextChange = { infoDate = it }
        )

        EditItem(
            text = stringResource(R.string.cost),
            accountState = accountState,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onTextChange = { infoCost = it }
        )

        EditItem(
            text = stringResource(R.string.classification),
            accountState = accountState,
            onTextChange = { infoClassification = it }
        )

        EditItem(
            text = stringResource(R.string.content),
            accountState = accountState,
            onTextChange = { infoContent = it }
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.height(10.dp))

        SaveButton(
            onSave = {
                onSave(
                    makeAccountItem(
                        kind = infoKind,
                        date = infoDate.takeIf { it.isNotBlank() } ?: getToday(),
                        cost = infoCost.substring(0, infoCost.lastIndex).toLong(),
                        classification = infoClassification,
                        content = infoContent
                    )
                )
            }
        )
    }
}

@Composable
fun EditItem(
    text: String,
    subText: String? = null,
    accountState: AccountState,
    keyboardOptions: KeyboardOptions? = null,
    onTextChange: (String) -> Unit
) {
    var content by remember { mutableStateOf(subText ?: "") }

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

        Spacer(modifier = Modifier.width(20.dp))

        val isCost = text == stringResource(R.string.cost)
        AccountComponentTextField(
            text = if(isCost && content.isNotBlank()) "${content}원" else content,
            focusedIndicatorColor = focusedIndicatorColor,
            keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
            onValueChange = {
                content = if(isCost && it.last() !in '0' .. '9') it.substring(0, it.length - 1) else it
                onTextChange(it)
            }
        )
    }
}

@Composable
fun SaveButton(
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(
                color = colorResource(R.color.gray_asparagus3),
                shape = RoundedCornerShape(16.dp)
            )
            .noRippleClick(
                onClick = onSave
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.save),
            color = colorResource(R.color.white)
        )
    }
}

fun makeAccountItem(
    kind: String,
    date: String,
    cost: Long,
    classification: String?,
    content: String?
): AccountEntity =
    AccountEntity(
        kind = kind,
        date = date,
        cost = cost,
        classification = classification,
        content = content
    )