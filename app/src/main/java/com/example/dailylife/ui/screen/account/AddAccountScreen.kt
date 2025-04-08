package com.example.dailylife.ui.screen.account

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import com.example.dailylife.R
import com.example.dailylife.component.AccountComponentTextField
import com.example.dailylife.component.AccountEditText
import com.example.dailylife.component.AccountSelectorBox
import com.example.dailylife.component.HorizontalDivider
import com.example.dailylife.state.AccountState
import com.example.dailylife.transformation.CostTransformation
import com.example.dailylife.transformation.DateTransformation
import com.example.dailylife.util.convertStandardDate
import com.example.dailylife.util.getToday
import com.example.dailylife.util.noRippleClick
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.util.showSnackbarShort
import com.example.dailylife.util.topBarModifier
import com.example.dailylife.viewmodel.AccountViewModel
import com.example.dailylife.viewmodel.AccountSelectorViewModel
import com.example.data.entitiy.AccountEntity
import com.example.domain.state.AccountContinuationState
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddAccountScreen(
    accountViewModel: AccountViewModel,
    accountSelectorViewModel: AccountSelectorViewModel,
    editAccountItem: AccountEntity?,
    goListEditScreen: (String) -> Unit,
    onBackAction: () -> Unit,
) {
    val context = LocalContext.current
    var accountState by remember { mutableStateOf(AccountState.INCOME) }
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isShowSelectAccountInfoBox by remember { mutableStateOf("") }

    BackHandler {
        if(isShowSelectAccountInfoBox.isNotBlank()) isShowSelectAccountInfoBox = ""
        else onBackAction()
    }

    LaunchedEffect(accountViewModel.accountContinuationError) {
        accountViewModel.accountContinuationError.collectLatest { error ->
            when(error) {
                AccountContinuationState.InValidDateFormat -> snackbarState.showSnackbarShort(scope, getString(context, R.string.invalid_date))
                AccountContinuationState.InvalidCostFormat -> snackbarState.showSnackbarShort(scope, getString(context, R.string.invalid_cost))
                else -> snackbarState.showSnackbarShort(scope, getString(context, R.string.error))
            }
        }
    }

    LaunchedEffect(accountViewModel.accountContinuationSuccess) {
        accountViewModel.accountContinuationSuccess.collectLatest {
            onBackAction()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.ivory))
            ) {
                AddAccountTopBarArea(
                    modifier = Modifier.topBarModifier(),
                    title = when(accountState) {
                        AccountState.INCOME -> stringResource(R.string.income)
                        AccountState.EXPEND -> stringResource(R.string.expend)
                        else -> stringResource(R.string.error)
                    },
                    clickBackButton = onBackAction
                )

                AccountButtonArea(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    accountState = accountState,
                    onClick = { state -> accountState = state }
                )

                HorizontalDivider()

                EditInfoArea(
                    modifier = Modifier.weight(1f),
                    accountState = accountState,
                    accountSelectorViewModel = accountSelectorViewModel,
                    editAccountItem = editAccountItem,
                    isShowSelectAccountInfoBox = isShowSelectAccountInfoBox,
                    onSave = { item ->
                        if(editAccountItem == null) accountViewModel.addAccountItem(item)
                        else accountViewModel.updateAccountItem(
                            editAccountItem.copy(
                                kind = item.kind,
                                date = item.date,
                                cost = item.cost,
                                classification = item.classification,
                                cardCompany = item.cardCompany,
                                content = item.content
                            )
                        )
                    },
                    onShowAccountInfoBox = { kind -> isShowSelectAccountInfoBox = kind },
                )
            }

            if(isShowSelectAccountInfoBox.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClick(onClick = { isShowSelectAccountInfoBox = "" }),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AccountSelectorBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                        title = isShowSelectAccountInfoBox,
                        goListEditScreen = goListEditScreen,
                        onClose = { isShowSelectAccountInfoBox = "" },
                        onButtonClick = {
                            when(isShowSelectAccountInfoBox) {
                                getString(context, R.string.classification) -> accountSelectorViewModel.setSelectedClassification(it)
                                getString(context, R.string.card_company) -> accountSelectorViewModel.setSelectedCardCompany(it)
                            }
                        },
                        accountSelectorViewModel = accountSelectorViewModel
                    )
                }
            }
        }
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
    isShowSelectAccountInfoBox: String,
    accountSelectorViewModel: AccountSelectorViewModel,
    editAccountItem: AccountEntity?,
    onSave: (AccountEntity) -> Unit,
    onShowAccountInfoBox: (String) -> Unit,
) {
    val context = LocalContext.current
    val infoKind = when(accountState) {
        AccountState.INCOME -> stringResource(R.string.income)
        AccountState.EXPEND -> stringResource(R.string.expend)
        else -> stringResource(R.string.error)
    }
    var infoDate by remember { mutableStateOf((editAccountItem?.date ?: getToday()).split("-").joinToString("")) }
    var infoCost by remember { mutableStateOf(editAccountItem?.cost?.toString() ?: "") }
    var infoClassification by remember { mutableStateOf(editAccountItem?.classification ?: "") }
    var infoCardCompany by remember { mutableStateOf(editAccountItem?.cardCompany ?: "") }
    var infoContent by remember { mutableStateOf(editAccountItem?.content ?: "") }

    LaunchedEffect(accountSelectorViewModel.selectedClassification) {
        accountSelectorViewModel.selectedClassification.collectLatest {
            infoClassification = it
        }
    }

    LaunchedEffect(accountSelectorViewModel.selectedCardCompany) {
        accountSelectorViewModel.selectedCardCompany.collectLatest {
            infoCardCompany = it
        }
    }

    Column(
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        EditItem(
            text = stringResource(R.string.date),
            subText = infoDate,
            accountState = accountState,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onTextChange = { infoDate = it },
            visualTransformation = DateTransformation()
        )

        EditItem(
            text = stringResource(R.string.cost),
            subText = infoCost,
            accountState = accountState,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onTextChange = { infoCost = it },
            visualTransformation = CostTransformation()
        )

        EditItem(
            text = stringResource(R.string.classification),
            subText = infoClassification,
            accountState = accountState,
            isTextField = false,
            isFocus = isShowSelectAccountInfoBox.isNotEmpty(),
            onShowAccountInfoBox = { onShowAccountInfoBox(getString(context, R.string.classification)) }
        )

        EditItem(
            text = stringResource(R.string.card_company),
            subText = infoCardCompany,
            accountState = accountState,
            isTextField = false,
            isFocus = isShowSelectAccountInfoBox.isNotEmpty(),
            onShowAccountInfoBox = { onShowAccountInfoBox(getString(context, R.string.card_company)) }
        )

        EditItem(
            text = stringResource(R.string.content),
            subText = infoContent,
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
                        date = infoDate.convertStandardDate(),
                        cost = if(infoCost.isEmpty()) 0 else infoCost.toLong(),
                        classification = getString(context, R.string.etc).takeIf { infoClassification.isBlank() } ?: infoClassification,
                        cardCompany = getString(context, R.string.money).takeIf { infoCardCompany.isBlank() } ?: infoCardCompany,
                        content = getString(context, R.string.etc).takeIf { infoContent.isBlank() } ?: infoContent
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
    visualTransformation: VisualTransformation? = null,
    isTextField: Boolean = true,
    isFocus: Boolean? = null,
    onTextChange: ((String) -> Unit)? = null,
    onShowAccountInfoBox: (() -> Unit)? = null
) {
    var textField by remember { mutableStateOf(TextFieldValue(subText ?: "")) }

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
                fontSize = 12.sp,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        if(isTextField) {
            AccountComponentTextField(
                text = textField.text,
                focusedIndicatorColor = focusedIndicatorColor,
                keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
                visualTransformation = visualTransformation ?: VisualTransformation.None,
                onValueChange = {
                    textField = TextFieldValue(it)
                    onTextChange?.invoke(it)
                }
            )
        } else {
            AccountEditText(
                modifier = Modifier.
                noRippleClick(
                    onClick = onShowAccountInfoBox!!
                ),
                text = subText?:"",
                isFocus = isFocus ?: false,
                focusedIndicatorColor = focusedIndicatorColor
            )
        }
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
                color = colorResource(R.color.gray_asparagus), shape = RoundedCornerShape(16.dp)
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
    classification: String,
    cardCompany: String,
    content: String
): AccountEntity =
    AccountEntity(
        kind = kind,
        date = date,
        cost = cost,
        classification = classification,
        cardCompany = cardCompany,
        content = content
    )