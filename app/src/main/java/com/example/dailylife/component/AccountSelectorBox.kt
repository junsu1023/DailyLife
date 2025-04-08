package com.example.dailylife.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailylife.R
import com.example.dailylife.util.clickableBlock
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.viewmodel.AccountSelectorViewModel
import com.example.data.entitiy.CardCompanyEntity
import com.example.data.entitiy.ClassificationEntity

@Composable
fun AccountSelectorBox(
    modifier: Modifier,
    title: String,
    goListEditScreen: (String) -> Unit,
    onClose: () -> Unit,
    onButtonClick: (String) -> Unit,
    accountSelectorViewModel: AccountSelectorViewModel
) {
    val classificationList by accountSelectorViewModel.chunkedClassificationList.collectAsStateWithLifecycle()
    val cardCompanyList by accountSelectorViewModel.chunkedCardCompanyList.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .background(colorResource(R.color.white))
            .clickableBlock()
    ) {
        HeaderArea(
            title = title,
            goListEditScreen = goListEditScreen,
            onClose = onClose
        )

        SelectorBodyArea(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.gray)),
            isSelectedClassification = title == stringResource(R.string.classification),
            classificationList = classificationList,
            cardCompanyList = cardCompanyList,
            onClose = onClose,
            onButtonClick = onButtonClick
        )
    }
}

@Composable
fun HeaderArea(
    title: String,
    goListEditScreen: (String) -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
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
                    onClick = { goListEditScreen(title) }
                )
        )

        Spacer(modifier = Modifier.width(20.dp))

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

        Spacer(modifier = Modifier.width(15.dp))
    }
}

@Composable
fun SelectorBodyArea(
    modifier: Modifier,
    isSelectedClassification: Boolean,
    classificationList: List<List<ClassificationEntity>>,
    cardCompanyList: List<List<CardCompanyEntity>>,
    onClose: () -> Unit,
    onButtonClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
    ) {
        if(isSelectedClassification) {
            classificationList.forEach { list ->
                Row {
                    list.forEach { classification ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(colorResource(R.color.white))
                                .border(width = 1.dp, color = colorResource(R.color.gray))
                                .roundRippleClickable(rippleColor = colorResource(R.color.black), onClick = {
                                    onButtonClick(classification.classification)
                                    onClose()
                                }),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = classification.classification,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light
                                )
                            )
                        }
                    }
                }
            }
        } else {
            cardCompanyList.forEach { list ->
                Row {
                    list.forEach { cardCompany ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(colorResource(R.color.white))
                                .border(width = 1.dp, color = colorResource(R.color.gray))
                                .roundRippleClickable(rippleColor = colorResource(R.color.black), onClick = {
                                    onButtonClick(cardCompany.cardCompany)
                                    onClose()
                                }),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cardCompany.cardCompany,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}