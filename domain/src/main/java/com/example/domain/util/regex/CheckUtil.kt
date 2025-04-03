package com.example.domain.util.regex

import java.util.regex.Pattern

class CheckUtil {
    companion object {
        private const val DATE_FILTER = """([1-9]\d{3}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])"""

        val DATE_REGEX: Pattern = Pattern.compile(DATE_FILTER)
    }
}