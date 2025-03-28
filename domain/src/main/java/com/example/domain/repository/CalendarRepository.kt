package com.example.domain.repository

import com.example.domain.model.TodoModel

interface CalendarRepository {
    suspend fun getTodoListOfDate(date: String): List<TodoModel>

    suspend fun getTodoListOfMonth(month: Int): List<TodoModel>
}