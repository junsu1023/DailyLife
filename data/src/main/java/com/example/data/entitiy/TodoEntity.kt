package com.example.data.entitiy

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list.db")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val dueDate: String,
    val isComplete: Boolean = false, // 0 -> 초기값, 1 -> 완료
    val icon: Bitmap? = null,
    val iconColor: Int? = null,
    val title: String? = null
)