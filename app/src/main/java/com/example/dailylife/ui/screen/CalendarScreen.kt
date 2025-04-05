package com.example.dailylife.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.event.Event
import com.example.dailylife.R
import com.example.dailylife.component.CheckDeleteDialog
import com.example.dailylife.component.IconSelectorContainer
import com.example.dailylife.component.TodoBottomSheet
import com.example.dailylife.component.TodoDatePickerDialog
import com.example.dailylife.state.CalendarSize
import com.example.dailylife.state.CalendarState
import com.example.dailylife.state.TodoState
import com.example.dailylife.state.rememberCalendarState
import com.example.dailylife.ui.screen.todo.TodoEditScreen
import com.example.dailylife.ui.screen.todo.TodoItemArea
import com.example.dailylife.ui.screen.todo.TodoListHeader
import com.example.dailylife.util.convertString
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.util.showSnackbarShort
import com.example.dailylife.util.topBarModifier
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.TodoEntity
import com.example.domain.state.FailedState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.abs

@Composable
fun CalendarScreen(
    todoViewModel: TodoViewModel,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    BoxWithConstraints {
        val halfHeight = remember { maxHeight / 2 }
        val fullHeight = remember { maxHeight }
        val calendarState = rememberCalendarState()

        var isShowSelectContainer by remember { mutableStateOf(false) }
        var isShowTodoEditScreen by remember { mutableStateOf(false) }
        var isShowBottomSheet by remember { mutableStateOf(false) }
        var isShowCheckDeleteDialog by remember { mutableStateOf(false) }
        val todoDialogState by todoViewModel.todoDialogState.collectAsStateWithLifecycle()

        var selectorContainerOffset by remember { mutableStateOf(Pair(Offset.Zero, Offset.Zero)) }
        var topBarHeight by remember { mutableFloatStateOf(0f) }
        var headerHeight by remember { mutableFloatStateOf(0f) }
        var iconSelectorContainerSize by remember { mutableStateOf(IntSize(0, 0)) }
        var updateTodoItem by remember { mutableStateOf<TodoEntity?>(null) }

        var calendarHeight by remember { mutableStateOf(if (calendarState.calendarSize == CalendarSize.FULL) fullHeight else halfHeight) }
        val animatedHeight by animateDpAsState(calendarHeight)

        LaunchedEffect(todoViewModel.todoItemContinuationError) {
            todoViewModel.todoItemContinuationError.collectLatest { throwable ->
                when(throwable) {
                    FailedState.FailedAdd -> snackbarHostState.showSnackbarShort(scope, getString(context, R.string.failed_add_todo))
                    FailedState.FailedDelete -> snackbarHostState.showSnackbarShort(scope, getString(context, R.string.failed_delete_todo))
                    FailedState.FailedUpdate -> snackbarHostState.showSnackbarShort(scope, getString(context, R.string.failed_update_todo))
                }
            }
        }

        Column(
            modifier = Modifier
                .background(colorResource(R.color.ivory))
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            calendarHeight = (calendarHeight + dragAmount.toDp()).coerceIn(halfHeight, fullHeight)
                        },
                        onDragEnd = {
                            when (calendarState.calendarSize) {
                                CalendarSize.HALF -> if (calendarHeight > halfHeight) {
                                    calendarState.calendarSize = CalendarSize.FULL
                                    calendarHeight = fullHeight
                                }
                                CalendarSize.FULL -> if (calendarHeight < fullHeight) {
                                    calendarState.calendarSize = CalendarSize.HALF
                                    calendarHeight = halfHeight
                                }
                            }
                        }
                    )
                }
        ) {
            CalendarArea(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeight),
                calendarState = calendarState,
                todoViewModel = todoViewModel,
                showAddBottomSheet = { isShowBottomSheet = true },
                onClick = {
                    calendarState.calendarSize = CalendarSize.HALF
                    calendarHeight = halfHeight
                },
                callBackTopBarHeight = { topBarHeight = it }
            )

            HorizontalDivider(
                color = colorResource(R.color.calendar_divider),
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fullHeight - animatedHeight)
            ) {
                DayDiffInfoArea(calendarState)

                TodoListOfDateArea(
                    todoViewModel = todoViewModel,
                    callBackOffset = {
                        selectorContainerOffset = it
                        isShowSelectContainer = true
                    },
                    callBackHeaderHeight = { headerHeight = it },
                    callBackTodoItem = { updateTodoItem = it },
                    callBackShowDialogState = { isShowCheckDeleteDialog = true },
                    showTodoEditScreen = { isShowTodoEditScreen = true }
                )
            }
        }

        if(isShowSelectContainer) {
            IconSelectorContainer(
                todoItem = updateTodoItem!!,
                todoViewModel = todoViewModel,
                selectorContainerOffset = selectorContainerOffset,
                topBarHeight = topBarHeight,
                headerHeight = headerHeight,
                iconSelectorContainerSize = iconSelectorContainerSize,
                maxHeight = maxHeight.value,
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
                showBlankSnackBar = {
                    scope.launch {
                        snackbarHostState.showSnackbarShort(scope, getString(context, R.string.blank_text))
                    }
                },
            )
        }

        if(isShowTodoEditScreen) {
            TodoEditScreen(
                modifier = Modifier.align(Alignment.Center),
                todoItem = updateTodoItem!!,
                todoViewModel = todoViewModel,
                calendarState = calendarState,
                hideTodoEditScreen = { isShowTodoEditScreen = false },
                showBlankSnackBar = {
                    scope.launch {
                        snackbarHostState.showSnackbarShort(scope, getString(context, R.string.blank_text))
                    }
                },
                onClick = { isShowTodoEditScreen = false }
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
    }
}

@Composable
fun CalendarArea(
    modifier: Modifier,
    calendarState: CalendarState,
    todoViewModel: TodoViewModel,
    showAddBottomSheet: () -> Unit,
    onClick: () -> Unit,
    callBackTopBarHeight: (Float) -> Unit,
) {
    LaunchedEffect(calendarState.datePagerState.currentPage) {
        if (calendarState.currentPageYM != YearMonth.from(calendarState.selectedDate)) {
            calendarState.selectedDate = calendarState.currentPageYM.atDay(1)
        }
    }

    LaunchedEffect(calendarState.selectedDate) {
        todoViewModel.setSelectedDate(calendarState.selectedDate.convertString()).join()
        todoViewModel.publishEvent(Event.NeedRefresh)

        if (calendarState.currentPageYM != YearMonth.from(calendarState.selectedDate)) {
            val nextPage = calendarState.datePagerState.currentPage + if (calendarState.selectedDate.isAfter(calendarState.currentPageYM.atEndOfMonth())) 1 else -1
            calendarState.datePagerState.animateScrollToPage(nextPage)
        }
    }

    val allTodoList by todoViewModel.allTodoList.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CalendarTopBarArea(
            pageYearAndMonth = calendarState.currentPageYM,
            showAddBottomSheet = showAddBottomSheet,
            callBackTopBarHeight = callBackTopBarHeight
        )

        HorizontalDivider(
            color = colorResource(R.color.platinum),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(10.dp))

        DaysArea()

        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = calendarState.datePagerState
        ) { page ->
            val pageYearMonth = remember { YearMonth.from(calendarState.currentDate).plusMonths((page - Int.MAX_VALUE / 2).toLong()) }
            val daysOfMonth = remember { calendarState.getDaysOfMonth(pageYearMonth) }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val weeks = daysOfMonth.chunked(7)

                weeks.forEachIndexed { idx, week ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        week.forEach { day ->
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                val isToday = remember(day, calendarState.currentDate) { day == calendarState.currentDate }
                                val isSelected = remember(day, calendarState.selectedDate) { day == calendarState.selectedDate }
                                val isVisibleMonth = remember { YearMonth.from(day).monthValue == pageYearMonth.monthValue }

                                CalendarDay(
                                    date = day,
                                    isToday = isToday,
                                    isSelected = isSelected,
                                    isVisibleMonth = isVisibleMonth,
                                    count = allTodoList.count { it.dueDate == day.convertString() },
                                    completeCount = allTodoList.count { it.dueDate == day.convertString() && it.isComplete },
                                    onClick = {
                                        calendarState.selectedDate = day
                                        onClick()
                                    }
                                )
                            }
                        }
                    }

                    if (idx != weeks.size - 1) {
                        HorizontalDivider(
                            color = colorResource(R.color.platinum),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarTopBarArea(
    pageYearAndMonth: YearMonth,
    showAddBottomSheet: () -> Unit,
    callBackTopBarHeight: (Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .topBarModifier()
            .onGloballyPositioned { layoutCoordinates ->
                callBackTopBarHeight(layoutCoordinates.size.height.toFloat())
            }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "${pageYearAndMonth.year}${stringResource(R.string.year)} ${pageYearAndMonth.monthValue}${stringResource(R.string.month)}",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = null,
                modifier = Modifier.roundRippleClickable(
                    rippleColor = colorResource(R.color.black),
                    onClick = showAddBottomSheet
                )
            )

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun DaysArea() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp)
    ) {
        val days = listOf(
            stringResource(R.string.sunday),
            stringResource(R.string.monday),
            stringResource(R.string.tuesday),
            stringResource(R.string.wednesday),
            stringResource(R.string.thursday),
            stringResource(R.string.friday),
            stringResource(R.string.saturday)
        )

        days.forEach { dayText ->
            Text(
                text = dayText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (dayText == stringResource(R.string.sunday)) colorResource(R.color.red) else colorResource(R.color.black),
                modifier = Modifier.weight(1f)
            )
        }
    }

    HorizontalDivider(
        color = colorResource(R.color.platinum),
        thickness = 1.dp
    )
}


@Composable
fun CalendarDay(
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    isVisibleMonth: Boolean,
    count: Int,
    completeCount: Int,
    onClick: () -> Unit
) {
    val red = colorResource(R.color.red)
    val black = colorResource(R.color.black)
    val darkGray = colorResource(R.color.dark_gray)
    val white = colorResource(R.color.white)

    val daysColor = remember(isToday, date, date.dayOfWeek) {
        when {
            isToday && date.dayOfWeek == DayOfWeek.SUNDAY -> red
            isToday -> black
            else -> Color.Transparent
        }
    }

    val textColor = remember(isToday, isVisibleMonth) {
        when {
            isToday -> white
            !isVisibleMonth && date.dayOfWeek == DayOfWeek.SUNDAY -> red.copy(alpha = 0.3f)
            !isVisibleMonth -> darkGray.copy(alpha = 0.3f)
            date.dayOfWeek == DayOfWeek.SUNDAY -> red
            else -> black
        }
    }

    Surface(
        onClick = onClick,
        color = colorResource(R.color.ivory),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, colorResource(R.color.black)).takeIf { isSelected }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 2.5.dp)
                    .size(20.dp)
                    .background(daysColor, CircleShape)
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColor
                )
            }

            if(count != 0) {
                Spacer(modifier = Modifier.height(10.dp))
                TodoCountArea(
                    todoCount = count,
                    completeCount = completeCount,
                    isVisibleMonth = isVisibleMonth
                )
            }
        }
    }
}

@Composable
fun TodoCountArea(
    todoCount: Int,
    completeCount: Int,
    isVisibleMonth: Boolean
) {
    if(todoCount == completeCount) {
        Icon(
            painter = painterResource(R.drawable.all_complete),
            contentDescription = null,
            tint = if(isVisibleMonth) colorResource(R.color.gray_asparagus) else colorResource(R.color.gray_asparagus2)
        )
    } else if(todoCount != 0) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (isVisibleMonth) colorResource(R.color.gray_asparagus) else colorResource(R.color.gray_asparagus2),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+${todoCount - completeCount}",
                color = colorResource(R.color.white),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun DayDiffInfoArea(
    calendarState: CalendarState
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 20.dp)
    ) {
        val daysDiff = calendarState.calDaysDiff()

        Text(
            text = when {
                daysDiff == 0L -> stringResource(R.string.calendar_today)
                daysDiff == 1L -> stringResource(R.string.calendar_tomorrow)
                daysDiff == -1L -> stringResource(R.string.calendar_yesterday)
                daysDiff > 0L -> "${daysDiff}${stringResource(R.string.calendar_after)}"
                else -> "${abs(daysDiff)}${stringResource(R.string.calendar_before)}"
            },
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TodoListOfDateArea(
    todoViewModel: TodoViewModel,
    callBackOffset: (Pair<Offset, Offset>) -> Unit,
    callBackHeaderHeight: (Float) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit,
    showTodoEditScreen: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val todoListOfDate by todoViewModel.todoListOfDate.collectAsStateWithLifecycle()
    val completeTodoListOfDate by todoViewModel.completeTodoListOfDate.collectAsStateWithLifecycle()
    val todoList = arrayOf(todoListOfDate to TodoState.TODO, completeTodoListOfDate to TodoState.DATE_COMPLETE)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        for(list in todoList) {
            if(list.first.isNotEmpty()) {
                CalendarTodoBundle(
                    kind = list.second,
                    list = list.first,
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
fun CalendarTodoBundle(
    kind: TodoState,
    list: List<TodoEntity>,
    updateTodoList: (TodoEntity) -> Unit,
    callBackOffset: (Pair<Offset, Offset>) -> Unit,
    callBackHeaderHeight: (Float) -> Unit,
    callBackTodoItem: (TodoEntity) -> Unit,
    callBackShowDialogState: () -> Unit,
    showTodoEditScreen: () -> Unit
) {
    TodoListHeader(
        state = kind,
        callBackHeaderHeight = callBackHeaderHeight
    ) {
        list.forEach { todo ->
            TodoItemArea(
                todoKind = kind,
                todoItem = todo,
                isCalendarItem = true,
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