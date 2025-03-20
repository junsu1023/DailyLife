package com.example.domain.model

import android.graphics.Bitmap

data class TodoModel(
    val id: Long? = null,
    val dueDate: String,
    val isComplete: Boolean = false,
    val icon: Bitmap? = null,
    val title: String? = null
)