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
    var snapState by mutableStateOf(CalendarSize.FULL)
    var selectedDate: LocalDate by mutableStateOf(LocalDate.now())
    val currentDate: LocalDate get() = LocalDate.now()
    val currentPageYM: YearMonth get() = YearMonth.from(currentDate)

    fun getDaysOfMonth(ym: YearMonth): List<LocalDate> {
        val startMonth = ym.atDay(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val endMonth = ym.atEndOfMonth().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

        return generateSequence(startMonth) { it.plusDays(1) }.takeWhile { !it.isAfter(endMonth) }.toList()
    }

    fun calDaysDiff(): String {
        val diff = selectedDate.toEpochDay() - currentDate.toEpochDay()
        return when {
            diff == 0L -> "오늘"
            diff == 1L -> "내일"
            diff == -1L -> "어제"
            diff > 0L -> "${diff}일 후"
            else -> "${diff}일 전"
        }
    }

    companion object {
        fun Saver(
            datePagerState: PagerState
        ): Saver<CalendarState, Any> = listSaver(
            save = {
                listOf(
                    it.snapState,
                    it.selectedDate
                )
            },
            restore = { savedValue ->
                CalendarState(
                    datePagerState = datePagerState
                ).apply {
                    snapState = savedValue[0] as CalendarSize
                    selectedDate = savedValue[1] as LocalDate
                }
            }
        )
    }
}

@Composable
fun rememberCalendarState(
    pagerState: PagerState = rememberPagerState(Int.MAX_VALUE / 2) { Int.MAX_VALUE },
): CalendarState {
    return rememberSaveable(
        pagerState,
        saver = CalendarState.Saver(pagerState)
    ) {
        CalendarState(
            datePagerState = pagerState
        )
    }
}