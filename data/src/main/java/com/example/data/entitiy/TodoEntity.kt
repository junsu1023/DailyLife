package com.example.data.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "todo_list.db")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val updateDate: Date = Date(System.currentTimeMillis()),
    val priority: Int? = null,
    val state: Int? = null, // 0 -> 초기, 1 -> 진행 중, 2 -> 완료
    val title: String? = null
)