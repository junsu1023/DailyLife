package com.example.dailylife.ui.screen.account

import android.annotation.SuppressLint
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.example.dailylife.R
import com.example.dailylife.util.showSnackbarShort
import com.example.dailylife.util.topBarModifier
import com.example.dailylife.viewmodel.AccountSelectorViewModel
import com.example.data.entitiy.CardCompanyEntity
import com.example.data.entitiy.ClassificationEntity
import com.example.domain.state.FailedState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditAddItemScreen(
    title: String,
    editKind: Any?,
    accountSelectorViewModel: AccountSelectorViewModel,
    onBackAction: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }
    val isAddMode = editKind == null

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
        snackbarHost = { SnackbarHost(hostState = snackbarState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.ivory))
        ) {
            EditListTopBarArea(
                modifier = Modifier.topBarModifier(),
                title = "$title ${stringResource(R.string.edit)}",
                isAddMode = true,
                clickBackButton = onBackAction
            )

            var text by remember {
                mutableStateOf(
                    when(editKind) {
                        is ClassificationEntity -> editKind.classification
                        is CardCompanyEntity -> editKind.cardCompany
                        else -> ""
                    }
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.input_add_item),
                            color = colorResource(R.color.gray)
                        )
                    },
                    trailingIcon = {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = text.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
                        ) {
                            IconButton(
                                onClick = { text = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Clear, contentDescription = null
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colorResource(R.color.dark_gray),
                        unfocusedIndicatorColor = colorResource(R.color.dark_gray)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                SaveButton(
                    onSave = {
                        if(text.isBlank()) {
                            scope.launch {
                                snackbarState.showSnackbarShort(scope, getString(context, R.string.blank_text))
                            }
                        } else {
                            when(isAddMode) {
                                true -> {
                                    if(title == getString(context, R.string.classification)) {
                                        accountSelectorViewModel.addClassification(ClassificationEntity(classification = text))
                                    } else {
                                        accountSelectorViewModel.addCardCompany(CardCompanyEntity(cardCompany = text))
                                    }
                                }
                                false -> {
                                    if(title == getString(context, R.string.classification)) {
                                        accountSelectorViewModel.updateClassification((editKind as ClassificationEntity).copy(classification = text))
                                    } else {
                                        accountSelectorViewModel.updateCardCompany((editKind as CardCompanyEntity).copy(cardCompany = text))
                                    }
                                }
                            }


                            onBackAction()
                        }
                    }
                )
            }
        }
    }
}