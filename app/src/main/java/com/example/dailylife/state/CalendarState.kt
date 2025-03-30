package com.example.dailylife.state

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

enum class CalendarSize {
    HALF, FULL
}

data class CalendarState(
    val datePagerState: PagerState
) {
    var calendarSize by mutableStateOf(CalendarSize.FULL)
    var selectedDate: LocalDate by mutableStateOf(LocalDate.now())
    val currentDate: LocalDate get() = LocalDate.now()
    val currentPageYM: YearMonth get() = YearMonth.from(currentDate).plusMonths(datePagerState.currentPage - Int.MAX_VALUE / 2L)

    fun getDaysOfMonth(ym: YearMonth): List<LocalDate> {
        val startOfMonth = ym.atDay(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val endOfMonth = ym.atEndOfMonth().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

        return generateSequence(startOfMonth) { it.plusDays(1) }.takeWhile { !it.isAfter(endOfMonth) }.toList()
    }

    fun calDaysDiff(): Long = selectedDate.toEpochDay() - currentDate.toEpochDay()

    companion object {
        fun Saver(
            datePagerState: PagerState
        ): Saver<CalendarState, Any> = listSaver(
            save = {
                listOf(
                    it.calendarSize,
                    it.selectedDate
                )
            },
            restore = { savedValue ->
                CalendarState(
                    datePagerState = datePagerState
                ).apply {
                    calendarSize = savedValue[0] as CalendarSize
                    selectedDate = savedValue[1] as LocalDate
                }
            }
        )
    }
}


@Composable
fun rememberCalendarState(
    datePagerState: PagerState = rememberPagerState(Int.MAX_VALUE / 2) { Int.MAX_VALUE },
): CalendarState {
    return rememberSaveable(
        datePagerState,
        saver = CalendarState.Saver(datePagerState)
    ) {
        CalendarState(
            datePagerState = datePagerState
        )
    }
}