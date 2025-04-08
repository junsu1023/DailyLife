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

fun YearMonth.convertTitleString(): String {
    val year = this.year
    val month = this.monthValue

    return String.format("$year / %02d", month)
}

fun YearMonth.convertDBString(): String {
    val year = this.year
    val month = this.monthValue

    return String.format("$year-%02d", month)
}

fun String.convertLocalDate(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ISO_DATE)

fun String.convertStandardDate(): String {
    if(this.isEmpty()) return getToday()
    if(this.count { it == '-' } == 2) return this

    var date = ""
    for(i in this.indices) {
        date += this[i]
        if(i == 3) date += '-'
        else if(i == 5) date += '-'
    }

    return date
}