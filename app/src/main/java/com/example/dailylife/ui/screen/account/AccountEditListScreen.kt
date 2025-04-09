package com.example.dailylife.ui.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailylife.R
import com.example.dailylife.component.HorizontalDivider
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.util.showSnackbarShort
import com.example.dailylife.util.topBarModifier
import com.example.dailylife.viewmodel.AccountSelectorViewModel
import com.example.domain.state.FailedState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AccountEditListScreen(
    title: String,
    accountSelectorViewModel: AccountSelectorViewModel,
    onBackAction: () -> Unit,
    goAddTitle: () -> Unit,
    onEditTitle: (Any) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }

    LaunchedEffect(accountSelectorViewModel.editContinuationError) {
        accountSelectorViewModel.editContinuationError.collectLatest { error ->
            when(error) {
                FailedState.FailedAdd -> snackbarState.showSnackbarShort(scope, getString(context, R.string.failed_add_todo))
                FailedState.FailedDelete -> snackbarState.showSnackbarShort(scope, getString(context, R.string.failed_delete_todo))
                FailedState.FailedUpdate -> snackbarState.showSnackbarShort(scope, getString(context, R.string.failed_update_todo))
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        topBar = {
            EditListTopBarArea(
                modifier = Modifier.topBarModifier(),
                title = "$title ${stringResource(R.string.edit)}",
                clickBackButton = onBackAction,
                goAddTitle = goAddTitle
            )
        },
        bottomBar = {
            Box(modifier = Modifier.size(0.dp))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.ivory))
                .padding(it)
        ) {
            if(title == stringResource(R.string.classification)) {
                EditClassificationListBodyArea(
                    accountSelectorViewModel = accountSelectorViewModel,
                    onEditTitle = onEditTitle
                )
            } else {
                EditCardCompanyListBodyArea(
                    accountSelectorViewModel = accountSelectorViewModel,
                    onEditTitle = onEditTitle
                )
            }
        }
    }
}

@Composable
fun EditListTopBarArea(
    modifier: Modifier,
    title: String,
    isAddMode: Boolean = false,
    clickBackButton: () -> Unit,
    goAddTitle: (() -> Unit)? = null
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
            tint = colorResource(R.color.black),
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
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                color = colorResource(R.color.black)
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        if(!isAddMode) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = null,
                tint = colorResource(R.color.black),
                modifier = Modifier.roundRippleClickable(
                    rippleColor = colorResource(R.color.black),
                    onClick = { goAddTitle?.invoke() }
                )
            )

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun EditClassificationListBodyArea(
    accountSelectorViewModel: AccountSelectorViewModel,
    onEditTitle: (Any) -> Unit
) {
    val list by accountSelectorViewModel.classificationList.collectAsStateWithLifecycle()

    HorizontalDivider(color = colorResource(R.color.gray))

    LazyColumn {
        itemsIndexed(
            items = list,
            key = { index, item -> "$index - $item "}
        ) { index, item ->
            ItemArea(
                title = item.classification,
                onDeleteItem = { accountSelectorViewModel.deleteClassification(item) },
                onEditTitle = { onEditTitle(item) }
            )

            HorizontalDivider(color = colorResource(R.color.gray))
        }
    }
}

@Composable
fun EditCardCompanyListBodyArea(
    accountSelectorViewModel: AccountSelectorViewModel,
    onEditTitle: (Any) -> Unit
) {
    val list by accountSelectorViewModel.cardCompanyList.collectAsStateWithLifecycle()

    HorizontalDivider(color = colorResource(R.color.gray))

    LazyColumn {
        itemsIndexed(
            items = list,
            key = { index, item -> "$index - $item "}
        ) { index, item ->
            ItemArea(
                title = item.cardCompany,
                onDeleteItem = { accountSelectorViewModel.deleteCardCompany(item) },
                onEditTitle = { onEditTitle(item) }
            )

            HorizontalDivider(color = colorResource(R.color.gray))
        }
    }
}

@Composable
fun ItemArea(
    title: String,
    onDeleteItem: () -> Unit,
    onEditTitle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painterResource(R.drawable.item_delete),
            contentDescription = null,
            tint = colorResource(R.color.red),
            modifier = Modifier.roundRippleClickable(
                rippleColor = colorResource(R.color.red),
                onClick = onDeleteItem
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            style = TextStyle(
                fontSize = 15.sp,
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                color = colorResource(R.color.black)
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.edit),
            contentDescription = null,
            tint = colorResource(R.color.dark_gray).copy(alpha = 0.3f),
            modifier = Modifier.roundRippleClickable(
                rippleColor = colorResource(R.color.dark_gray).copy(alpha = 0.3f),
                onClick = onEditTitle
            )
        )

        Spacer(modifier = Modifier.width(10.dp))
    }
}