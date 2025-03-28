package com.example.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.data.entitiy.TodoEntity

@Dao
interface CalendarDao {
    @Query("select * from `todo_list.db` where substr(dueDate, 6, 7) = :month")
    fun getTodoListOfMonth(month: Int): List<TodoEntity>

    @Query("select * from `todo_list.db` where dueDate = :date")
    fun getTodoListOfDate(date: String): List<TodoEntity>
}