package com.example.domain.repository

import com.example.domain.model.TodoModel

interface TodoRepository {
    suspend fun getTodoList(): List<TodoModel>

    suspend fun getTodayTodoList(): List<TodoModel>

    suspend fun getFutureTodoList(): List<TodoModel>

    suspend fun getTodayCompleteTodoList(): List<TodoModel>

    suspend fun addTodoModel(todoModel: TodoModel): Result<Unit>

    suspend fun deleteTodoModel(todoModel: TodoModel): Result<Unit>

    suspend fun updateTodoList(todoModel: TodoModel): Result<Unit>
}