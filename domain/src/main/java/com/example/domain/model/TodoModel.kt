package com.example.domain.model

import android.graphics.Bitmap
import java.util.Date

data class TodoModel(
    val id: Long? = null,
    val dueDate: Date = Date(System.currentTimeMillis()),
    val isComplete: Boolean = false,
    val icon: Bitmap? = null,
    val title: String? = null
)