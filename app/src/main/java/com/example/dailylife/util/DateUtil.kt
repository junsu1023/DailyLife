package com.example.dailylife.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

fun getToday(): String {
    val curTime = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    return dateFormat.format(curTime)
}

fun LocalDate.convertString(): String {
    val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    dtf.format(this)

    return dtf.format(this)
}

fun YearMonth.convertString(): String {
    val year = this.year
    val month = this.monthValue

    return "${year} / ${month}"
}