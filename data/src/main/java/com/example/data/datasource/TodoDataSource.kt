package com.example.data.datasource

import com.example.data.dao.TodoDao
import com.example.data.entitiy.TodoEntity

class TodoDataSource(
    private val todoListDao: TodoDao,
) {
    fun getTodoList(): List<TodoEntity> = todoListDao.getTodoList()

    fun getTodayTodoList(): List<TodoEntity> = todoListDao.getTodayTodoList()

    fun getFutureTodoList(): List<TodoEntity> = todoListDao.getFutureTodoList()

    fun getTodayCompleteTodoList(): List<TodoEntity> = todoListDao.getTodayCompleteTodoList()

    fun addTodoEntity(todoEntity: TodoEntity) = todoListDao.addTodoEntity(todoEntity)

    fun deleteTodoEntity(todoEntity: TodoEntity) = todoListDao.deleteTodoEntity(todoEntity)
}