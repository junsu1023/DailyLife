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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailylife.R
import com.example.dailylife.state.CalendarSize
import com.example.dailylife.state.CalendarState
import com.example.dailylife.state.rememberCalendarState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalenderScreen() {
    val configuration = LocalConfiguration.current
    val state = rememberCalendarState()

    BoxWithConstraints {
        val halfHeight = remember { maxHeight / 2 }
        val fullHeight = remember { maxHeight }
        var calendarHeight by remember { mutableStateOf(if (state.snapState == CalendarSize.FULL) fullHeight else halfHeight) }
        val animatedHeight by animateDpAsState(calendarHeight)

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
                            when (state.snapState) {
                                CalendarSize.HALF -> if (calendarHeight > halfHeight) {
                                    state.snapState = CalendarSize.FULL
                                    calendarHeight = fullHeight
                                }
                                CalendarSize.FULL -> if (calendarHeight < fullHeight) {
                                    state.snapState = CalendarSize.HALF
                                    calendarHeight = halfHeight
                                }
                            }
                        }
                    )
                }
        ) {
            CalendarArea(
                state = state,
                onClick = {
                    state.snapState = CalendarSize.HALF
                    calendarHeight = halfHeight
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeight)
            )

            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp,
                modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 0.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(configuration.screenHeightDp.dp - animatedHeight)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = state.calDaysDiff(),
                        fontSize = 17.sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        modifier = Modifier
                            .size(2.dp)
                            .background(colorResource(R.color.black), CircleShape)
                    )

                    Text(
                        text = state.selectedDate.format(DateTimeFormatter.ofPattern("M. d. (E)").withLocale(Locale.forLanguageTag("ko"))),
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarArea(
    modifier: Modifier,
    state: CalendarState,
    onClick: () -> Unit
) {
    LaunchedEffect(state.datePagerState.currentPage) {
        if(state.currentPageYM != YearMonth.from(state.selectedDate)) {
            state.selectedDate = YearMonth.from(state.selectedDate).atDay(1)
        }
    }

    LaunchedEffect(state.selectedDate) {
        state.currentPageYM.takeIf { it != YearMonth.from(state.selectedDate) }?.let { pageMonth -> state.datePagerState
            .animateScrollToPage(state.datePagerState.currentPage + (1.takeIf { state.selectedDate.isAfter(pageMonth.atEndOfMonth()) } ?: -1)) }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CalendarHeader(state = state)

        DaysArea()

        HorizontalPager(
            state = state.datePagerState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) { page ->
            val pageYearMonth = remember { YearMonth.from(state.currentDate).plusMonths((page - Int.MAX_VALUE / 2).toLong()) }
            val dayOfMonth = remember { state.getDaysOfMonth(pageYearMonth) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                dayOfMonth.chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        week.forEach { date ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                val isToday = remember(date, state.currentDate) { date == state.currentDate }
                                val isSelected = remember(date, state.selectedDate) { date == state.selectedDate }
                                val isVisibleMonth = remember { YearMonth.from(date) == pageYearMonth }

                                CalendarDay(
                                    date = date,
                                    isToday = isToday,
                                    isSelected = isSelected,
                                    isVisibleMonth = isVisibleMonth,
                                    isCurrentMonth = true,
                                    onClick = {
                                        state.selectedDate = date
                                        onClick()
                                    }
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarHeader(
    state: CalendarState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = state.currentPageYM.run {
                "${year}${stringResource(R.string.year)} ${monthValue}${stringResource(R.string.month)}"
            },
            fontSize = 22.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
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
                color = if(dayText == stringResource(R.string.sunday)) colorResource(R.color.red) else colorResource(R.color.black),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun CalendarDay(
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    isVisibleMonth: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    val red = colorResource(R.color.red)
    val white = colorResource(R.color.white)
    val black = colorResource(R.color.black)
    val darkGray = colorResource(R.color.dark_gray)

    val daysColor = remember(isToday, date, date.dayOfWeek) {
        when {
            isToday && date.dayOfWeek == DayOfWeek.SUNDAY -> red
            isToday -> black
            else -> Color.Transparent
        }
    }

    val textColor = remember(isToday, isVisibleMonth) {
        when {
            isToday -> Color.White
            !isCurrentMonth && date.dayOfWeek == DayOfWeek.SUNDAY -> red.copy(alpha = 0.3f)
            !isCurrentMonth -> darkGray.copy(alpha = 0.3f)
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
        }
    }
}