package com.example.domain.repository

import com.example.domain.model.TodoModel

interface TodoRepository {
    suspend fun getAllTodoList(): List<TodoModel>

    suspend fun getTodoListOfToday(): List<TodoModel>

    suspend fun getTodoListOfFuture(): List<TodoModel>

    suspend fun getCompleteTodoListOfToday(): List<TodoModel>

    suspend fun getTodoListOfPrev(): List<TodoModel>

    suspend fun getTodoListOfDate(date: String): List<TodoModel>

    suspend fun getCompleteTodoListOfDate(date: String): List<TodoModel>

    suspend fun addTodoModel(todoModel: TodoModel): Result<Unit>

    suspend fun deleteTodoModel(todoModel: TodoModel): Result<Unit>

    suspend fun updateTodoList(todoModel: TodoModel): Result<Unit>
}