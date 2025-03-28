package com.example.data.repository

import com.example.data.datasource.CalendarDataSource
import com.example.data.mapper.convertTodoModel
import com.example.domain.model.TodoModel
import com.example.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarDataSource: CalendarDataSource
): CalendarRepository {
    override suspend fun getTodoListOfDate(date: String): List<TodoModel> =
        calendarDataSource.getTodoListOfDate(date).map { it.convertTodoModel() }

    override suspend fun getTodoListOfMonth(month: Int): List<TodoModel> =
        calendarDataSource.getTodoListOfMonth(month).map { it.convertTodoModel() }
}