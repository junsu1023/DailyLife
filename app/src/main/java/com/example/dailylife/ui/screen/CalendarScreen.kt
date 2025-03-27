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
import kotlin.math.abs

@Composable
fun CalendarScreen() {
    val configuration = LocalConfiguration.current

    BoxWithConstraints {
        val halfHeight = remember { maxHeight / 2 }
        val fullHeight = remember { maxHeight }
        val calendarState = rememberCalendarState()

        var calendarHeight by remember { mutableStateOf(if (calendarState.calendarSize == CalendarSize.FULL) fullHeight else halfHeight) }
        val animatedHeight by animateDpAsState(calendarHeight)

        Column(
            modifier = Modifier
                .background(colorResource(R.color.ivory))
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            calendarHeight =
                                (calendarHeight + dragAmount.toDp()).coerceIn(
                                    halfHeight,
                                    fullHeight
                                )
                        }, onDragEnd = {
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
                onClick = {
                    calendarState.calendarSize = CalendarSize.HALF
                    calendarHeight = halfHeight
                }
            )

            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp,
                modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp)
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
        }
    }
}

@Composable
fun CalendarArea(
    modifier: Modifier,
    calendarState: CalendarState,
    onClick: () -> Unit
) {
    LaunchedEffect(calendarState.datePagerState.currentPage) {
        if (calendarState.currentPageYM != YearMonth.from(calendarState.selectedDate)) {
            calendarState.selectedDate = calendarState.currentPageYM.atDay(1)
        }
    }

    LaunchedEffect(calendarState.selectedDate) {
        if (calendarState.currentPageYM != YearMonth.from(calendarState.selectedDate)) {
            val nextPage = calendarState.datePagerState.currentPage + if (calendarState.selectedDate.isAfter(calendarState.currentPageYM.atEndOfMonth())) 1 else -1
            calendarState.datePagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CalendarHeader(
            pageYearAndMonth = calendarState.currentPageYM
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
                                val isVisibleMonth = remember { YearMonth.from(day) == calendarState.currentPageYM }
                                val isCurrentMonth = remember { YearMonth.from(day).monthValue == calendarState.currentDate.monthValue }

                                CalendarDay(
                                    date = day,
                                    isToday = isToday,
                                    isSelected = isSelected,
                                    isVisibleMonth = isVisibleMonth,
                                    isCurrentMonth = isCurrentMonth,
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
fun CalendarHeader(
    pageYearAndMonth: YearMonth
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(R.color.bone))
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "${pageYearAndMonth.year}${stringResource(R.string.year)} ${pageYearAndMonth.monthValue}${stringResource(R.string.month)}",
            fontSize = 22.sp,
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
    isCurrentMonth: Boolean,
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