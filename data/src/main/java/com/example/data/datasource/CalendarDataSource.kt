package com.example.data.datasource

import com.example.data.dao.CalendarDao
import com.example.data.entitiy.TodoEntity

class CalendarDataSource(
    private val calendarDao: CalendarDao
) {
    fun getTodoListOfMonth(month: Int): List<TodoEntity> = calendarDao.getTodoListOfMonth(month)

    fun getTodoListOfDate(date: String): List<TodoEntity> = calendarDao.getTodoListOfDate(date)
}