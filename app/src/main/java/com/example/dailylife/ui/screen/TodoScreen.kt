package com.example.dailylife.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailylife.R
import com.example.dailylife.component.CheckBox
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.TodoEntity
import java.util.Date

fun getDummyList(str: String): List<TodoEntity> {
    val list = mutableListOf<TodoEntity>()
    repeat(3) {
        list.add(
            TodoEntity(
                updateDate = Date(System.currentTimeMillis()),
                priority = 0,
                state = 0,
                title = "$str${it + 1}"
            )
        )
    }
    return list
}

@Composable
fun TodoScreen(
    todoViewModel: TodoViewModel
) {
    Box {
        Column(
            modifier = Modifier
        ) {
            TodoTopBarArea()

            Spacer(modifier = Modifier.height(5.dp))

            TodoListArea(todoViewModel)
        }
    }
}

@Composable
fun TodoTopBarArea() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(R.color.bone)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.fighting),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = stringResource(R.string.todo_origin),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(5.dp))

        Icon(
            painter = painterResource(R.drawable.fighting),
            contentDescription = null
        )
    }
}

@Composable
fun TodoListArea(todoViewModel: TodoViewModel) {
    val todayTodoList by todoViewModel.todayTodoList.collectAsStateWithLifecycle()
    val futureTodoList by todoViewModel.futureTodoList.collectAsStateWithLifecycle()
    val todayCompleteTodoList by todoViewModel.todayCompleteTodoList.collectAsStateWithLifecycle()

    val dummyTodayTodoList = getDummyList("today") // test - today dummy list
    val dummyFutureTodoList = getDummyList("future") // test - future dummy list
    val dummyTodayCompleteTodoList = getDummyList("complete") // test - complete dummy list

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        if(dummyTodayTodoList.isNotEmpty()) {
            TodoListItem(
                todoList = dummyTodayTodoList,
                state = 1
            )
        }

        if(dummyFutureTodoList.isNotEmpty()) {
            TodoListItem(
                todoList = dummyFutureTodoList,
                state = 2
            )
        }

        if(dummyTodayCompleteTodoList.isNotEmpty()) {
            TodoListItem(
                todoList = dummyTodayCompleteTodoList,
                state = 3
            )
        }
    }
}

@Composable
private fun TodoListItem(
    todoList: List<TodoEntity>,
    state: Int
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when(state) {
                    1 -> stringResource(R.string.today)
                    2 -> stringResource(R.string.future)
                    else -> stringResource(R.string.today_complete)
                }
            )

            TodoItemButton(
                isExpanded = isExpanded,
                onClick = { isExpanded = !isExpanded }
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        if(isExpanded) {
            /*
            TODO
            LazyColumn으로 변경
            중첩 스크롤로 인한 IllegalStateException으로 우선 Column으로 처리함.
             */
            todoList.forEach { todo ->
                TodoItem(todo)
                Spacer(modifier = Modifier.height(5.dp))
            }
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun TodoItemButton(
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(
                enabled = true,
                onClick = onClick
            )
    ) {
        Icon(
            if(isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun TodoItem(
    todoItem: TodoEntity
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = colorResource(R.color.platinum),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))

            CheckBox(
                checked = todoItem.state == 2,
                onCheckChanged = {
                    /*
                    TODO
                    viewModel에 완료 method 추가
                     */
                }
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = todoItem.title ?: stringResource(R.string.does_not_exist),
            )

            Spacer(modifier = Modifier.weight(1f))

            /*
            TODO
            Icon 넣어서 선택 가능하도록 추가
             */
        }
    }
}