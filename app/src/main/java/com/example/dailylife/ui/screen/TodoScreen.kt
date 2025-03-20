package com.example.dailylife.ui.screen

import android.view.MotionEvent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
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
import com.example.dailylife.component.IconSelectorContainer
import com.example.dailylife.component.TodoBottomSheet
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.TodoEntity
import kotlinx.coroutines.launch

@Composable
fun TodoScreen(
    todoViewModel: TodoViewModel
) {
    val density = LocalDensity.current
    var isShowSelectContainer by remember { mutableStateOf(false) }
    var selectorContainerOffset by remember { mutableStateOf(Offset.Zero) }
    var topBarHeight by remember { mutableStateOf(0f) }
    var headerHeight by remember { mutableStateOf(0f) }
    var iconSelectorContainerWidth by remember { mutableStateOf(0f) }
    val onClick: (Offset) -> Unit = {
        selectorContainerOffset = it
        isShowSelectContainer = true
    }

    var isShowBottomSheet by remember { mutableStateOf(false) }
    val lifecycleScope = rememberCoroutineScope()

    Box {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TodoTopBarArea(
                callBackTopBarHeight = { topBarHeight = it }
            )

            Spacer(modifier = Modifier.height(5.dp))

            TodoContentArea(
                modifier = Modifier.weight(1f),
                todoViewModel = todoViewModel,
                onClick = onClick,
                callBackHeaderHeight = { headerHeight = it }
            )
        }

        AddTodoButtonArea(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 20.dp),
            onClick = { isShowBottomSheet = true }
        )

        if(isShowSelectContainer) {
            val offsetX = with(density) { (selectorContainerOffset.x - iconSelectorContainerWidth / 2).toDp() }
            val offsetY = with(density) { (selectorContainerOffset.y - topBarHeight + headerHeight).toDp() }

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
                    IconSelectorContainer(
                        callBackContainerWidth = { iconSelectorContainerWidth = it }
                    )
                }
            }
        }

        if(isShowBottomSheet) {
            TodoBottomSheet(
                closeSheet = { isShowBottomSheet = false },
                onSaveTodo = { todoItem ->
                    lifecycleScope.launch {
                        todoViewModel.addTodoList(todoItem)
                        todoViewModel.refreshTodoList()
                    }
                }
            )
        }
    }
}

@Composable
fun TodoTopBarArea(
    callBackTopBarHeight: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(R.color.bone))
            .onGloballyPositioned { layoutCoordinates ->
                callBackTopBarHeight(layoutCoordinates.size.height.toFloat())
            },
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
    onClick: (Offset) -> Unit,
    callBackHeaderHeight: (Float) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        TodoListContent(
            todoViewModel = todoViewModel,
            onClick = onClick,
            callBackHeaderHeight = callBackHeaderHeight
        )
    }
}

@Composable
fun TodoListContent(
    todoViewModel: TodoViewModel,
    onClick: (Offset) -> Unit,
    callBackHeaderHeight: (Float) -> Unit
) {
    val todoList by todoViewModel.todoList.collectAsStateWithLifecycle()
    val todayTodoList by todoViewModel.todayTodoList.collectAsStateWithLifecycle()
    val futureTodoList by todoViewModel.futureTodoList.collectAsStateWithLifecycle()
    val todayCompleteTodoList by todoViewModel.todayCompleteTodoList.collectAsStateWithLifecycle()

    val todoListKind = arrayOf(todayTodoList, futureTodoList, todayCompleteTodoList)
    LaunchedEffect(todoList, todayTodoList, futureTodoList, todayCompleteTodoList) {
        todoViewModel.refreshTodoList()
    }

    val scrollState = rememberScrollState()
    val lifecycleScope = rememberCoroutineScope()

    if(todayTodoList.isEmpty() && futureTodoList.isEmpty() && todayCompleteTodoList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.blank_background),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
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
                    updateTodoList = {
                        lifecycleScope.launch {
                            todoViewModel.updateTodoList(it)
                            todoViewModel.refreshTodoList()
                        }
                    },
                    onClick = onClick,
                    callBackHeaderHeight = callBackHeaderHeight
                )
            }
        }
    }
}

@Composable
fun TodoBundle(
    state: Int,
    list: List<TodoEntity>,
    updateTodoList: (TodoEntity) -> Unit,
    onClick: (Offset) -> Unit,
    callBackHeaderHeight: (Float) -> Unit
) {
    TodoListHeader(
        state = state,
        callBackHeaderHeight = callBackHeaderHeight
    ) {
        list.forEach { todo ->
            TodoItemArea(
                todoItem = todo,
                updateTodoList = updateTodoList,
                onClick = onClick
            )

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun TodoListHeader(
    state: Int,
    callBackHeaderHeight: (Float) -> Unit,
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
                    dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
                )
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    callBackHeaderHeight(layoutCoordinates.size.height.toFloat())
                },
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
    updateTodoList: (TodoEntity) -> Unit,
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
                updateTodoList(todoItem.copy(isComplete = it))
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