package com.example.dailylife.ui.screen.todo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailylife.R
import com.example.dailylife.component.CheckBox
import com.example.dailylife.component.CheckDeleteDialog
import com.example.dailylife.component.IconSelectorContainer
import com.example.dailylife.component.TodoBottomSheet
import com.example.dailylife.component.TodoDatePickerDialog
import com.example.dailylife.component.TodoSnackBar
import com.example.dailylife.state.TodoState
import com.example.dailylife.util.convertDrawableToBitMap
import com.example.dailylife.util.getToday
import com.example.dailylife.util.headerModifier
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.util.topBarModifier
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.TodoEntity
import com.example.domain.state.FailedState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun TodoScreen(
    todoViewModel: TodoViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // show component state
    var isShowSelectContainer by remember { mutableStateOf(false) }
    var isShowCheckDeleteDialog by remember { mutableStateOf(false) }
    var isShowBottomSheet by remember { mutableStateOf(false) }
    var isShowTodoEditScreen by remember { mutableStateOf(false) }

    // get size state
    var selectorContainerOffset by remember { mutableStateOf(Pair(Offset.Zero, Offset.Zero)) }
    var topBarHeight by remember { mutableFloatStateOf(0f) }
    var headerHeight by remember { mutableFloatStateOf(0f) }
    var iconSelectorContainerSize by remember { mutableStateOf(IntSize(0, 0)) }
    var fullSizeHeight by remember { mutableFloatStateOf(0f) }

    val todoDialogState by todoViewModel.todoDialogState.collectAsStateWithLifecycle()
    var updateTodoItem by remember { mutableStateOf<TodoEntity?>(null) }
    var isDeleteMode by remember { mutableStateOf(false) }
    val callBackOffset: (Pair<Offset, Offset>) -> Unit = {
        selectorContainerOffset = it
        isShowSelectContainer = true
    }
    var showSnackbarMessage by remember { mutableStateOf<String?>(null) }

    SideEffect {
        scope.launch {
            todoViewModel.todoItemContinuationError.collectLatest { throwable ->
                when(throwable) {
                    FailedState.FailedAdd -> showSnackbarMessage = getString(context, R.string.failed_add_todo)
                    FailedState.FailedDelete -> showSnackbarMessage = getString(context, R.string.failed_delete_todo)
                    FailedState.FailedUpdate -> showSnackbarMessage = getString(context, R.string.failed_update_todo)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .onGloballyPositioned { layoutCoordinates ->
                val fullHeight = layoutCoordinates.size.height.toFloat()
                fullSizeHeight = fullHeight
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TodoTopBarArea(
                isDeleteMode = isDeleteMode,
                callBackTopBarHeight = { topBarHeight = it },
                executeDeleteMode = { isDeleteMode = !isDeleteMode }
            )

            HorizontalDivider(
                color = colorResource(R.color.platinum),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(5.dp))

            TodoContentArea(
                modifier = Modifier.weight(1f),
                todoViewModel = todoViewModel,
                callBackOffset = callBackOffset,
                isDeleteMode = isDeleteMode,
                callBackHeaderHeight = { headerHeight = it },
                callBackTodoItem = { updateTodoItem = it },
                callBackShowDialogState = { isShowCheckDeleteDialog = true },
                showTodoEditScreen = { isShowTodoEditScreen = true }
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
            IconSelectorContainer(
                todoItem = updateTodoItem!!,
                todoViewModel = todoViewModel,
                selectorContainerOffset = selectorContainerOffset,
                topBarHeight = topBarHeight,
                headerHeight = headerHeight,
                iconSelectorContainerSize = iconSelectorContainerSize,
                maxHeight = fullSizeHeight,
                callBackContainerSize = { iconSelectorContainerSize = it },
                isShowSelectContainer = { isShowSelectContainer = it }
            )
        }

        if(isShowBottomSheet) {
            TodoBottomSheet(
                todoViewModel = todoViewModel,
                closeSheet = { isShowBottomSheet = false },
                onSaveTodo = { todoItem ->
                    todoViewModel.addTodoList(todoItem)
                },
                showBlankSnackBar = { showSnackbarMessage = getString(context, R.string.blank_text) },
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
                        setSelectedDate(date)
                    }
                },
                onClickCancel = { todoViewModel.hiddenTodoDateDialog() }
            )
        }

        if(isShowTodoEditScreen) {
            TodoEditScreen(
                modifier = Modifier.align(Alignment.Center),
                todoItem = updateTodoItem!!,
                todoViewModel = todoViewModel,
                hideTodoEditScreen = { isShowTodoEditScreen = false },
                showBlankSnackBar = { showSnackbarMessage = getString(context, R.string.blank_text) },
                onClick = { isShowTodoEditScreen = false }
            )
        }

        if(showSnackbarMessage != null) {
            TodoSnackBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = showSnackbarMessage!!,
                changedState = { showSnackbarMessage = null }
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
            .topBarModifier()
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
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
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
                .roundRippleClickable(rippleColor = colorResource(R.color.black), onClick = { executeDeleteMode() })
        )
    }
}

@Composable
fun TodoContentArea(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel,
    callBackOffset: (Pair<Offset, Offset>) -> Unit,
    isDeleteMode: Boolean,
    callBackHeaderHeight: (Float) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit,
    showTodoEditScreen: () -> Unit
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
            callBackShowDialogState = callBackShowDialogState,
            showTodoEditScreen = showTodoEditScreen
        )
    }
}

@Composable
fun TodoListContent(
    todoViewModel: TodoViewModel,
    isDeleteMode: Boolean,
    callBackOffset: (Pair<Offset, Offset>) -> Unit,
    callBackHeaderHeight: (Float) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit,
    showTodoEditScreen: () -> Unit
) {
    val prevTodoList by todoViewModel.todoListOfPrev.collectAsStateWithLifecycle()
    val todayTodoList by todoViewModel.todoListOfToday.collectAsStateWithLifecycle()
    val futureTodoList by todoViewModel.todoListOfFuture.collectAsStateWithLifecycle()
    val todayCompleteTodoList by todoViewModel.completeTodoListOfToday.collectAsStateWithLifecycle()
    val todoListKind = arrayOf(prevTodoList, todayTodoList, futureTodoList, todayCompleteTodoList)
    val todoState = arrayOf(TodoState.PREV, TodoState.TODAY, TodoState.FUTURE, TodoState.COMPLETE)

    val scrollState = rememberScrollState()

    if(prevTodoList.isEmpty() && todayTodoList.isEmpty() && futureTodoList.isEmpty() && todayCompleteTodoList.isEmpty()) {
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
        repeat(4) { idx ->
            if (todoListKind[idx].isNotEmpty()) {
                TodoBundle(
                    state = todoState[idx],
                    list = todoListKind[idx],
                    isDeleteMode = isDeleteMode,
                    updateTodoList = { todoViewModel.updateTodoList(it) },
                    callBackOffset = callBackOffset,
                    callBackHeaderHeight = callBackHeaderHeight,
                    callBackTodoItem = callBackTodoItem,
                    callBackShowDialogState = callBackShowDialogState,
                    showTodoEditScreen = showTodoEditScreen
                )
            }
        }
    }
}

@Composable
fun TodoBundle(
    state: TodoState,
    list: List<TodoEntity>,
    isDeleteMode: Boolean,
    updateTodoList: (TodoEntity) -> Unit,
    callBackOffset: (Pair<Offset, Offset>) -> Unit,
    callBackHeaderHeight: (Float) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit,
    showTodoEditScreen: () -> Unit
) {
    TodoListHeader(
        state = state,
        callBackHeaderHeight = callBackHeaderHeight
    ) {
        list.forEach { todo ->
            TodoItemArea(
                todoKind = state,
                todoItem = todo,
                isDeleteMode = isDeleteMode,
                updateTodoList = updateTodoList,
                callBackOffset = callBackOffset,
                callBackTodoItem = callBackTodoItem,
                callBackShowDialogState = callBackShowDialogState,
                showTodoEditScreen = showTodoEditScreen
            )

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun TodoListHeader(
    state: TodoState,
    callBackHeaderHeight: (Float) -> Unit,
    content: @Composable (() -> Unit)
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.headerModifier(),
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
                    TodoState.PREV -> stringResource(R.string.prev)
                    TodoState.TODAY -> stringResource(R.string.today)
                    TodoState.FUTURE -> stringResource(R.string.future)
                    TodoState.COMPLETE -> stringResource(R.string.today_complete)
                    TodoState.TODO -> stringResource(R.string.Work)
                    TodoState.DATE_COMPLETE -> stringResource(R.string.complete)
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
            .roundRippleClickable(
                rippleColor = colorResource(R.color.black),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoItemArea(
    todoKind: TodoState,
    todoItem: TodoEntity,
    isDeleteMode: Boolean = false,
    isCalendarItem: Boolean = false,
    updateTodoList: (TodoEntity) -> Unit,
    callBackOffset: (Pair<Offset, Offset>) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit,
    showTodoEditScreen: () -> Unit
) {
    var offset by remember { mutableStateOf(Pair(Offset.Zero, Offset.Zero)) }
    val context = LocalContext.current
    val bitmapIcon = todoItem.icon ?: convertDrawableToBitMap(context, R.drawable.default_icon)
    val iconColor = if(todoItem.iconColor == null) LocalContentColor.current else colorResource(todoItem.iconColor!!)
    val backgroundColor = if(todoItem.isComplete) colorResource(R.color.gray_asparagus3) else colorResource(R.color.gray_asparagus2)
    val today = getToday()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isCalendarItem) 40.dp else 50.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    showTodoEditScreen()
                    callBackTodoItem(todoItem)
                },
                onLongClick = {
                    if(isCalendarItem) callBackShowDialogState()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))

        CheckBox(
            modifier = Modifier
                .size(if(isCalendarItem) 18.dp else 24.dp),
            checked = todoItem.isComplete,
            onCheckChanged = {
                when(todoKind) {
                    TodoState.PREV,
                    TodoState.FUTURE,
                    TodoState.TODO -> {
                        updateTodoList(todoItem.copy(isComplete = it, prevDueDate = todoItem.dueDate, dueDate = today))
                    }
                    TodoState.COMPLETE,
                    TodoState.DATE_COMPLETE -> {
                        updateTodoList(todoItem.copy(isComplete = it, dueDate = todoItem.prevDueDate ?: today))
                    }
                    TodoState.TODAY -> updateTodoList(todoItem.copy(isComplete = it, prevDueDate = todoItem.dueDate))
                }
            }
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = todoItem.title ?: stringResource(R.string.does_not_exist),
            fontSize = if(isCalendarItem) 15.sp else TextUnit.Unspecified
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            bitmap = if(isDeleteMode) convertDrawableToBitMap(context, R.drawable.delete_icon)!!.asImageBitmap() else bitmapIcon!!.asImageBitmap(),
            contentDescription = null,
            tint = if(isDeleteMode) colorResource(R.color.black) else iconColor,
            modifier = Modifier
                .size(if (isCalendarItem) 18.dp else 24.dp)
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
                    val bottomLeft = layoutCoordinates.boundsInRoot().bottomLeft
                    val topLeft = layoutCoordinates.boundsInRoot().topLeft

                    offset = Pair(bottomLeft, topLeft)
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
        painter = painterResource(R.drawable.add2),
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