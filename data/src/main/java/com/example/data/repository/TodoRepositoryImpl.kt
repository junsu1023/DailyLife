package com.example.data.repository

import com.example.data.datasource.TodoDataSource
import com.example.data.mapper.convertTodoEntity
import com.example.data.mapper.convertTodoModel
import com.example.domain.model.TodoModel
import com.example.domain.repository.TodoRepository
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDataSource: TodoDataSource
): TodoRepository {
    override suspend fun getTodoList(): List<TodoModel> =
        todoDataSource.getTodoList().map { it.convertTodoModel() }

    override suspend fun getTodayTodoList(): List<TodoModel> =
        todoDataSource.getTodayTodoList().map { it.convertTodoModel() }

    override suspend fun getFutureTodoList(): List<TodoModel> =
        todoDataSource.getFutureTodoList().map { it.convertTodoModel() }

    override suspend fun getTodayCompleteTodoList(): List<TodoModel> =
        todoDataSource.getTodayCompleteTodoList().map { it.convertTodoModel() }

    override suspend fun addTodoModel(todoModel: TodoModel): Result<Unit> = try {
        todoDataSource.addTodoEntity(todoModel.convertTodoEntity())
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(t)
    }

    override suspend fun deleteTodoModel(todoModel: TodoModel): Result<Unit> = try {
        todoDataSource.deleteTodoEntity(todoModel.convertTodoEntity())
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(t)
    }
}