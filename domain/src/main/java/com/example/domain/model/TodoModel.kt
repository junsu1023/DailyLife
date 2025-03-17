package com.example.domain.model

import java.util.Date

data class TodoModel(
    val id: Long? = null,
    val updateDate: Date = Date(System.currentTimeMillis()),
    val priority: Int? = null,
    val state: Int? = null,
    val title: String? = null
)