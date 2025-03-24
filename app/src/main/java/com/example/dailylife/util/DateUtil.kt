package com.example.dailylife.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun getToday(): String {
    val curTime = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    return dateFormat.format(curTime)
}