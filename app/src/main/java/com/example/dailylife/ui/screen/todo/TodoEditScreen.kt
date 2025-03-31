package com.example.dailylife.ui.screen.todo

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.event.Event
import com.example.dailylife.R
import com.example.dailylife.component.TextField
import com.example.dailylife.state.CalendarState
import com.example.dailylife.util.convertString
import com.example.dailylife.util.noRippleClick
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.TodoEntity

@Composable
fun TodoEditScreen(
    modifier: Modifier,
    todoItem: TodoEntity,
    todoViewModel: TodoViewModel,
    calendarState: CalendarState? = null,
    hideTodoEditScreen: () -> Unit,
    showBlankSnackBar: () -> Unit,
    onClick: () -> Unit
) {
    val date by todoViewModel.selectedDate.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClick(onClick = onClick)
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .background(
                    color = colorResource(R.color.white), shape = RoundedCornerShape(15.dp)
                )
        ) {
            var text by remember { mutableStateOf(todoItem.title) }

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = text ?: stringResource(R.string.blank_text),
                onValueChanged = { text = it },
                placeholderText = stringResource(R.string.input_edit_job),
                backgroundColor = colorResource(R.color.gray_asparagus2),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${stringResource(R.string.due_date)}:  $date"
                )

                Icon(
                    painter = painterResource(R.drawable.calendar_icon),
                    contentDescription = null,
                    tint = colorResource(R.color.gray_asparagus3),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 20.dp)
                        .roundRippleClickable(
                            rippleColor = colorResource(R.color.gray_asparagus3),
                            onClick = {
                                todoViewModel.updateTodoDate(todoItem.dueDate)
                                todoViewModel.showTodoDateDialog()
                            }
                        )
                )

                Spacer(modifier = Modifier.width(10.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))


            Row(
                modifier = Modifier.padding(end = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier
                        .roundRippleClickable(
                            rippleColor = colorResource(R.color.black),
                            onClick = {
                                hideTodoEditScreen()
                            }
                        ),
                    text = stringResource(R.string.cancel)
                )

                Text(
                    modifier = Modifier
                        .roundRippleClickable(
                            rippleColor = colorResource(R.color.black),
                            onClick = {
                                if(text.isNullOrEmpty()) {
                                    showBlankSnackBar()
                                } else {
                                    todoViewModel.updateTodoList(
                                        todoItem.copy(
                                            title = text,
                                            prevDueDate = if(todoItem.dueDate != date) todoItem.dueDate else date,
                                            dueDate = date
                                        )
                                    )

                                    if(calendarState != null) {
                                        todoViewModel.run {
                                            setSelectedDate(calendarState.selectedDate.convertString())
                                            publishEvent(Event.NeedRefresh)
                                        }
                                    }
                                }

                                hideTodoEditScreen()
                            }
                        ),
                    text = stringResource(R.string.ok)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}