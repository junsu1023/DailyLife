package com.example.dailylife.component

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dailylife.R
import com.example.dailylife.util.convertDrawableToBitMap
import com.example.dailylife.util.getToday
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.TodoEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoBottomSheet(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel,
    closeSheet: () -> Unit,
    onSaveTodo: (TodoEntity) -> Unit,
    showBlankSnackBar: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    var selectedDate by remember { mutableStateOf<String?>(null) }

    SideEffect {
        scope.launch {
            todoViewModel.selectedDate.collectLatest {
                selectedDate = it
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            selectedDate = null
        }
    }

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        modifier = Modifier,
        onDismissRequest = closeSheet,
        dragHandle = null,
        sheetState = sheetState,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = colorResource(R.color.alabaster)
    ) {
        Column(
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
        ) {
            var text by remember { mutableStateOf("") }

            TextField(
                text = text,
                onValueChanged = { text = it },
                placeholderText = stringResource(R.string.input_new_job),
                backgroundColor = colorResource(R.color.light_gray),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Icon(
                    painter = painterResource(R.drawable.calender_icon),
                    contentDescription = null,
                    tint = colorResource(R.color.gray_asparagus3),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 20.dp)
                        .roundRippleClickable(
                            rippleColor = colorResource(R.color.gray_asparagus3),
                            onClick = {
                                val today = getToday()
                                val date = selectedDate ?: today

                                todoViewModel.updateTodoDate(date)
                                todoViewModel.showTodoDateDialog()
                            }
                        )
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(R.drawable.check_icon),
                    contentDescription = null,
                    tint = colorResource(R.color.forest_green),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                        .roundRippleClickable(
                            rippleColor = colorResource(R.color.forest_green),
                            onClick = {
                                closeSheet()
                                if(text.isEmpty()) {
                                    showBlankSnackBar()
                                } else {
                                    onSaveTodo(makeTodoItem(context, R.drawable.default_icon, text, todoViewModel.todoDialogState.value.selectedDate))
                                    todoViewModel.updateTodoDate(getToday())
                                }
                            }
                        )
                )
            }
        }
    }
}

private fun makeTodoItem(
    context: Context,
    @DrawableRes resId: Int,
    title: String,
    dueDate: String?
): TodoEntity {
    val today = getToday()
    val date = dueDate ?: today

    return TodoEntity(
        dueDate = date,
        prevDueDate = null,
        isComplete = false,
        icon = convertDrawableToBitMap(context, resId),
        title = title
    )
}