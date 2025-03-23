package com.example.dailylife.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailylife.R
import com.example.dailylife.component.CheckBox
import com.example.dailylife.component.CheckDeleteDialog
import com.example.dailylife.component.IconSelectorContainer
import com.example.dailylife.component.TodoBottomSheet
import com.example.dailylife.component.TodoDatePickerDialog
import com.example.dailylife.component.TodoSnackBar
import com.example.dailylife.util.convertDrawableToBitMap
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.TodoEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun TodoScreen(
    todoViewModel: TodoViewModel
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // show component state
    var isShowSelectContainer by remember { mutableStateOf(false) }
    var isShowCheckDeleteDialog by remember { mutableStateOf(false) }
    var isShowSnackbar by remember { mutableStateOf(0) }
    var isShowBottomSheet by remember { mutableStateOf(false) }

    // get size state
    var selectorContainerOffset by remember { mutableStateOf(Offset.Zero) }
    var topBarHeight by remember { mutableStateOf(0f) }
    var headerHeight by remember { mutableStateOf(0f) }
    var iconSelectorContainerWidth by remember { mutableStateOf(0f) }

    val todoDialogState by todoViewModel.todoDialogState.collectAsStateWithLifecycle()
    var updateTodoItem by remember { mutableStateOf<TodoEntity?>(null) }
    var isDeleteMode by remember { mutableStateOf(false) }
    val callBackOffset: (Offset) -> Unit = {
        selectorContainerOffset = it
        isShowSelectContainer = true
    }

    SideEffect {
        scope.launch {
            with(todoViewModel) {
                addTodoItemContinuationError.collectLatest {
                    isShowSnackbar = 1
                }

                deleteTodoItemContinuationError.collectLatest {
                    isShowSnackbar = 2
                }

                updateTodoListContinuationError.collectLatest {
                    isShowSnackbar = 3
                }
            }
        }
    }

    Box {
        if(isShowSnackbar != 0) {
            TodoSnackBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = when(isShowSnackbar) {
                    1 -> stringResource(R.string.failed_add_todo)
                    2 -> stringResource(R.string.failed_delete_todo)
                    3 -> stringResource(R.string.failed_update_todo)
                    else -> stringResource(R.string.blank_text)
                },
                changedState = { isShowSnackbar = 0 }
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TodoTopBarArea(
                isDeleteMode = isDeleteMode,
                callBackTopBarHeight = { topBarHeight = it },
                executeDeleteMode = { isDeleteMode = !isDeleteMode }
            )

            Spacer(modifier = Modifier.height(5.dp))

            TodoContentArea(
                modifier = Modifier.weight(1f),
                todoViewModel = todoViewModel,
                callBackOffset = callBackOffset,
                isDeleteMode = isDeleteMode,
                callBackHeaderHeight = { headerHeight = it },
                callBackTodoItem = { updateTodoItem = it },
                callBackShowDialogState = { isShowCheckDeleteDialog = true }
            )
        }

        if(!isDeleteMode) {
            AddTodoButtonArea(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 20.dp),
                onClick = { isShowBottomSheet = true }
            )
        }

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
                        todoItem = updateTodoItem!!,
                        todoViewModel = todoViewModel,
                        callBackContainerWidth = { iconSelectorContainerWidth = it }
                    )
                }
            }
        }

        if(isShowBottomSheet) {
            TodoBottomSheet(
                todoViewModel = todoViewModel,
                closeSheet = { isShowBottomSheet = false },
                onSaveTodo = { todoItem ->
                    todoViewModel.addTodoList(todoItem)
                },
                showBlankSnackBar = { isShowSnackbar = 4 },
            )
        }

        if(isShowCheckDeleteDialog) {
            CheckDeleteDialog(
                title = stringResource(R.string.delete_dialog_title),
                description = stringResource(R.string.delete_dialog_description),
                onClickCancel = {
                    isShowCheckDeleteDialog = false
                },
                onClickConfirm = {
                    todoViewModel.deleteTodoList(updateTodoItem!!)
                    isShowCheckDeleteDialog = false
                }
            )
        }

        if(todoDialogState.isShowDialog) {
            TodoDatePickerDialog(
                selectedDate = todoDialogState.selectedDate,
                onClickConfirm = { date ->
                    with(todoViewModel) {
                        hiddenTodoDateDialog()
                        updateTodoDate(date)
                    }
                },
                onClickCancel = { todoViewModel.hiddenTodoDateDialog() }
            )
        }
    }
}

@Composable
fun TodoTopBarArea(
    isDeleteMode: Boolean,
    callBackTopBarHeight: (Float) -> Unit,
    executeDeleteMode: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(R.color.bone))
            .onGloballyPositioned { layoutCoordinates ->
                callBackTopBarHeight(layoutCoordinates.size.height.toFloat())
            }
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
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

        Icon(
            painter = if(isDeleteMode) painterResource(R.drawable.add) else painterResource(R.drawable.delete_menu_icon),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
                .roundRippleClickable(
                    rippleColor = colorResource(R.color.black),
                    onClick = { executeDeleteMode() }
                )
        )
    }
}

@Composable
fun TodoContentArea(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel,
    callBackOffset: (Offset) -> Unit,
    isDeleteMode: Boolean,
    callBackHeaderHeight: (Float) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        TodoListContent(
            todoViewModel = todoViewModel,
            isDeleteMode = isDeleteMode,
            callBackOffset = callBackOffset,
            callBackHeaderHeight = callBackHeaderHeight,
            callBackTodoItem = callBackTodoItem,
            callBackShowDialogState = callBackShowDialogState
        )
    }
}

@Composable
fun TodoListContent(
    todoViewModel: TodoViewModel,
    isDeleteMode: Boolean,
    callBackOffset: (Offset) -> Unit,
    callBackHeaderHeight: (Float) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit
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
                    isDeleteMode = isDeleteMode,
                    updateTodoList = {
                        todoViewModel.updateTodoList(it)
                    },
                    callBackOffset = callBackOffset,
                    callBackHeaderHeight = callBackHeaderHeight,
                    callBackTodoItem = callBackTodoItem,
                    callBackShowDialogState = callBackShowDialogState
                )
            }
        }
    }
}

@Composable
fun TodoBundle(
    state: Int,
    list: List<TodoEntity>,
    isDeleteMode: Boolean,
    updateTodoList: (TodoEntity) -> Unit,
    callBackOffset: (Offset) -> Unit,
    callBackHeaderHeight: (Float) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit
) {
    TodoListHeader(
        state = state,
        callBackHeaderHeight = callBackHeaderHeight
    ) {
        list.forEach { todo ->
            TodoItemArea(
                todoItem = todo,
                isDeleteMode = isDeleteMode,
                updateTodoList = updateTodoList,
                callBackOffset = callBackOffset,
                callBackTodoItem = callBackTodoItem,
                callBackShowDialogState = callBackShowDialogState
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
    isDeleteMode: Boolean,
    updateTodoList: (TodoEntity) -> Unit,
    callBackOffset: (Offset) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit,
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    val context = LocalContext.current
    val bitmapIcon = todoItem.icon ?: convertDrawableToBitMap(context, R.drawable.default_icon)
    val iconColor = if(todoItem.iconColor == null) LocalContentColor.current else colorResource(todoItem.iconColor!!)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = if (todoItem.isComplete) {
                    colorResource(R.color.gray_asparagus3)
                } else {
                    colorResource(R.color.gray_asparagus2)
                },
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
            bitmap = if(isDeleteMode) convertDrawableToBitMap(context, R.drawable.delete_icon)!!.asImageBitmap() else bitmapIcon!!.asImageBitmap(),
            contentDescription = null,
            tint = if(isDeleteMode) colorResource(R.color.black) else iconColor,
            modifier = Modifier
                .then(
                    if (isDeleteMode) {
                        Modifier.roundRippleClickable(
                            rippleColor = colorResource(R.color.black),
                            onClick = {
                                callBackShowDialogState()
                                callBackTodoItem(todoItem)
                            }
                        )
                    } else {
                        Modifier.roundRippleClickable(
                            rippleColor = iconColor,
                            onClick = {
                                callBackOffset(offset)
                                callBackTodoItem(todoItem)
                            }
                        )
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
    Icon(
        painter = painterResource(R.drawable.add_todo),
        contentDescription = null,
        tint = colorResource(R.color.gray_asparagus),
        modifier = modifier
            .background(
                color = Color.Transparent, shape = CircleShape
            )
            .size(40.dp)
            .roundRippleClickable(
                rippleColor = colorResource(R.color.gray_asparagus),
                onClick = onClick
            )
    )
}