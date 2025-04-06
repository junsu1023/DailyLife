package com.example.dailylife.ui.screen.account

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailylife.R
import com.example.dailylife.component.AccountCommonText
import com.example.dailylife.component.CheckDeleteDialog
import com.example.dailylife.state.AccountState
import com.example.dailylife.ui.screen.todo.TodoExpandButton
import com.example.dailylife.util.convertLocalDate
import com.example.dailylife.util.convertTitleString
import com.example.dailylife.util.headerModifier
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.viewmodel.AccountViewModel
import com.example.data.entitiy.AccountEntity
import java.time.YearMonth
import java.util.Locale

@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel,
    onClickAddButton: () -> Unit
) {
    var isShowCheckDeleteDialog by remember { mutableStateOf(false) }
    var updateAccountItem by remember { mutableStateOf<AccountEntity?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.ivory))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val currentYM by accountViewModel.currentYM.collectAsStateWithLifecycle()
            val accountOfDateGroup by accountViewModel.accountOfDateGroup.collectAsStateWithLifecycle()

            var totalIncome by remember { mutableStateOf(0L) }
            var totalExpend by remember { mutableStateOf(0L) }
            val strIncome = stringResource(R.string.income)
            val strExpend = stringResource(R.string.expend)

            LaunchedEffect(currentYM, accountOfDateGroup) {
                totalIncome = accountViewModel.getTotal(strIncome)
                totalExpend = accountViewModel.getTotal(strExpend)
            }

            AccountTopBarArea(
                ym = currentYM,
                increaseMonth = { accountViewModel.increaseCurrentYM() },
                decreaseMonth = { accountViewModel.decreaseCurrentYM() }
            )

            HorizontalDivider()

            AccountInfoArea(
                totalIncome = totalIncome,
                totalExpend = totalExpend
            )

            HorizontalDivider()

            AccountContentArea(
                modifier = Modifier.weight(1f),
                accountOfDateGroup = accountOfDateGroup,
                callBackShowDialogState = { isShowCheckDeleteDialog = true },
                callBackAccountItem = { updateAccountItem = it },
            )
        }

        AccountAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 10.dp),
            onClick = onClickAddButton
        )

        if(isShowCheckDeleteDialog) {
            CheckDeleteDialog(
                title = stringResource(R.string.delete_dialog_title),
                description = stringResource(R.string.delete_dialog_description),
                onClickCancel = {
                    isShowCheckDeleteDialog = false
                },
                onClickConfirm = {
                    accountViewModel.deleteAccountItem(updateAccountItem!!)
                    isShowCheckDeleteDialog = false
                }
            )
        }
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
                text = ym.convertTitleString(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
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
private fun AccountInfoArea(
    totalIncome: Long,
    totalExpend: Long
) {
    val consumptionInfo = arrayOf(
        AccountState.INCOME to totalIncome,
        AccountState.EXPEND to totalExpend,
        AccountState.TOTAL to totalIncome - totalExpend
    )

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
    consumption: Pair<AccountState, Long>
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
                rippleColor = colorResource(R.color.gray_asparagus), onClick = onClick
            )
    )
}

@Composable
private fun AccountContentArea(
    modifier: Modifier,
    accountOfDateGroup: Map<String, List<AccountEntity>>,
    callBackShowDialogState: () -> Unit,
    callBackAccountItem: (AccountEntity) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        if(accountOfDateGroup.isEmpty()) {
            BlankArea()
        } else {
            AccountBundle(
                modifier = Modifier.fillMaxSize(),
                accountOfDateGroup = accountOfDateGroup,
                callBackShowDialogState = callBackShowDialogState,
                callBackAccountItem = callBackAccountItem
            )
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
private fun AccountBundle(
    modifier: Modifier,
    accountOfDateGroup: Map<String, List<AccountEntity>>,
    callBackShowDialogState: () -> Unit,
    callBackAccountItem: (AccountEntity) -> Unit
) {
    val listState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(listState)
    ) {
        accountOfDateGroup.forEach { dateToEntity ->
            val curDate = dateToEntity.key
            val curDateList = dateToEntity.value
            val totalIncome = curDateList.filter { it.kind == stringResource(R.string.income) }.sumOf { it.cost }
            val totalExpend = curDateList.sumOf { it.cost } - totalIncome

            AccountItemHeader(
                date = curDate.substring(5),
                dayOfWeek = curDate.convertLocalDate().dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.KOREAN),
                totalIncome = totalIncome,
                totalExpend = totalExpend
            ) {
                curDateList.forEach { accountItem ->
                    AccountItemBody(
                        accountItem = accountItem,
                        callBackShowDialogState = callBackShowDialogState,
                        callBackAccountItem = callBackAccountItem
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    HorizontalDivider(
                        color = colorResource(R.color.platinum)
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@Composable
private fun AccountItemHeader(
    date: String,
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
        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(5.dp))

            Box(
                modifier = Modifier.background(
                    color = colorResource(R.color.steal_blue).copy(0.3f),
                    shape = RoundedCornerShape(12.dp)
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayOfWeek,
                    fontSize = 10.sp
                )
            }

            TodoExpandButton(
                isExpanded = isExpanded,
                onClick = { isExpanded = !isExpanded }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = totalIncome.toString(),
                style = TextStyle(
                    color = colorResource(R.color.medium_blue),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Box(
                modifier = Modifier.width(15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "|",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Text(
                text = totalExpend.toString(),
                style = TextStyle(
                    color = colorResource(R.color.red),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.width(5.dp))
        }

        if(isExpanded) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(5.dp))
            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountItemBody(
    accountItem: AccountEntity,
    callBackShowDialogState: () -> Unit,
    callBackAccountItem: (AccountEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 20.dp)
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    callBackShowDialogState()
                    callBackAccountItem(accountItem)
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AccountCommonText(
            text = accountItem.classification,
            color = colorResource(R.color.dark_gray),
            modifier = Modifier.width(30.dp)
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column {
            AccountCommonText(
                text = accountItem.content,
                color = colorResource(R.color.dark_gray)
            )

            AccountCommonText(
                text = accountItem.cardCompany,
                fontSize = 12.sp,
                color = colorResource(R.color.dark_gray)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        val textColor = colorResource(R.color.medium_blue).takeIf { accountItem.kind == stringResource(R.string.income) }?: colorResource(R.color.red)
        AccountCommonText(
            text = accountItem.cost.toString(),
            color = textColor
        )
    }
}