package com.example.data.datasource

import com.example.data.dao.TodoDao
import com.example.data.entitiy.TodoEntity

class TodoDataSource(
    private val todoListDao: TodoDao,
) {
    fun getTodoListOfToday(): List<TodoEntity> = todoListDao.getTodoListOfToday()

    fun getTodoListOfFuture(): List<TodoEntity> = todoListDao.getTodoListOfFuture()

    fun getCompleteTodoListOfToday(): List<TodoEntity> = todoListDao.getCompleteTodoListOfToday()

    fun getTodoListOfPrev(): List<TodoEntity> = todoListDao.getTodoListOfPrev()

    fun getTodoListOfDate(date: String): List<TodoEntity> = todoListDao.getTodoListOfDate(date)

    fun getCompleteTodoListOfDate(date: String): List<TodoEntity> = todoListDao.getCompleteTodoListOfDate(date)

    fun getTodoListOfMonth(month: Int): List<TodoEntity> = todoListDao.getTodoListOfMonth(month)

    fun addTodoEntity(todoEntity: TodoEntity) = todoListDao.addTodoEntity(todoEntity)

    fun deleteTodoEntity(todoEntity: TodoEntity) = todoListDao.deleteTodoEntity(todoEntity)

    fun updateTodoList(todoEntity: TodoEntity) = todoListDao.updateTodoList(todoEntity)
}