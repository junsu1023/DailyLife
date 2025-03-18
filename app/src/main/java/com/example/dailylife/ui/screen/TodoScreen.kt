package com.example.dailylife.ui.screen

import android.util.MutableInt
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
                dueDate = Date(System.currentTimeMillis()),
                isComplete = false,
                icon = null,
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
    val density = LocalDensity.current
    var isShowSelectContainer by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val onClick: (Offset) -> Unit = {
        offset = it
        isShowSelectContainer = true
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TodoTopBarArea()

            Spacer(modifier = Modifier.height(5.dp))

            TodoContentArea(
                modifier = Modifier.weight(1f),
                todoViewModel = todoViewModel,
                onClick = onClick
            )
        }

        AddTodoButtonArea(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 20.dp),
            onClick = {
                /*
                TODO
                바텀 시트 올라오게 하기
                 */
            }
        )

        if(isShowSelectContainer) {
            val offsetX = with(density) { offset.x.toDp() }
            val offsetY = with(density) { offset.y.toDp() }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isShowSelectContainer = false }
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = offsetX, y = offsetY)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.Red)
                    )
                }
            }
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
fun TodoContentArea(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel,
    onClick: (Offset) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        TodoListContent(
            todoViewModel = todoViewModel,
            onClick = onClick
        )
    }
}

@Composable
fun TodoListContent(
    todoViewModel: TodoViewModel,
    onClick: (Offset) -> Unit
) {
    val todayTodoList by todoViewModel.todayTodoList.collectAsStateWithLifecycle()
    val futureTodoList by todoViewModel.futureTodoList.collectAsStateWithLifecycle()
    val todayCompleteTodoList by todoViewModel.todayCompleteTodoList.collectAsStateWithLifecycle()

    val dummyTodayTodoList = getDummyList("today") // test - today dummy list
    val dummyFutureTodoList = getDummyList("future") // test - future dummy list
    val dummyTodayCompleteTodoList = getDummyList("complete") // test - complete dummy
    val todoListKind = arrayOf(dummyTodayTodoList, dummyFutureTodoList, dummyTodayCompleteTodoList)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        repeat(3) { idx ->
            if (todoListKind[idx].isNotEmpty()) {
                TodoBundle(
                    state = idx + 1,
                    list = todoListKind[idx],
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun TodoBundle(
    state: Int,
    list: List<TodoEntity>,
    onClick: (Offset) -> Unit
) {
    TodoListHeader(
        state = state
    ) {
        list.forEach { todo ->
            TodoItemArea(
                todoItem = todo,
                onClick = onClick
            )

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun TodoListHeader(
    state: Int,
    content: @Composable (() -> Unit)
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
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

            TodoExpandButton(
                isExpanded = isExpanded,
                onClick = { isExpanded = !isExpanded }
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        if(isExpanded) {
            content()
        }
    }
}

@Composable
fun TodoExpandButton(
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
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
fun TodoItemArea(
    todoItem: TodoEntity,
    onClick: (Offset) -> Unit
) {
    var offset by remember { mutableStateOf(Offset.Zero) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = colorResource(R.color.gray_asparagus2),
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))

        CheckBox(
            checked = todoItem.isComplete,
            onCheckChanged = {
                /*
                TODO
                viewModel - 체크 시 로직 추가
                 */
            }
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = todoItem.title ?: stringResource(R.string.does_not_exist)
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.default_icon),
            contentDescription = null,
            modifier = Modifier
                .clickable(
                    enabled = true,
                    onClick = {
                        onClick(offset)
                    }
                )
                .onGloballyPositioned { layoutCoordinates ->
                    offset = layoutCoordinates.boundsInRoot().bottomLeft
                }
        )

        Spacer(modifier = Modifier.width(30.dp))
    }
}

@Composable
fun AddTodoButtonArea(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = rememberRipple(
        bounded = false,
        color = colorResource(R.color.gray_asparagus)
    )

    Icon(
        painter = painterResource(R.drawable.add_todo),
        contentDescription = null,
        tint = colorResource(R.color.gray_asparagus),
        modifier = modifier
            .background(
                color = Color.Transparent,
                shape = CircleShape
            )
            .size(40.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple,
                onClick = onClick
            )
    )
}