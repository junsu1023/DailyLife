package com.example.domain.util.ext

import com.example.domain.util.regex.CheckUtil.Companion.DATE_REGEX

fun String.isAvailableDate(): Boolean = DATE_REGEX.matcher(this).matches()