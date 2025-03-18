package com.example.data.entitiy

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "todo_list.db")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val dueDate: Date = Date(System.currentTimeMillis()),
    val isComplete: Boolean = false, // 0 -> 초기값, 1 -> 완료
    val icon: Bitmap? = null,
    val title: String? = null
)
