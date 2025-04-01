package com.example.data.repository

import com.example.data.datasource.TodoDataSource
import com.example.data.mapper.convertTodoEntity
import com.example.data.mapper.convertTodoModel
import com.example.domain.model.TodoModel
import com.example.domain.repository.TodoRepository
import com.example.domain.state.FailedState
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDataSource: TodoDataSource
): TodoRepository {
    override suspend fun getAllTodoList(): List<TodoModel> =
        todoDataSource.getAllTodoList().map { it.convertTodoModel() }

    override suspend fun getTodoListOfToday(): List<TodoModel> =
        todoDataSource.getTodoListOfToday().map { it.convertTodoModel() }

    override suspend fun getTodoListOfFuture(): List<TodoModel> =
        todoDataSource.getTodoListOfFuture().map { it.convertTodoModel() }

    override suspend fun getCompleteTodoListOfToday(): List<TodoModel> =
        todoDataSource.getCompleteTodoListOfToday().map { it.convertTodoModel() }

    override suspend fun getTodoListOfPrev(): List<TodoModel> =
        todoDataSource.getTodoListOfPrev().map { it.convertTodoModel() }

    override suspend fun getTodoListOfDate(date: String): List<TodoModel> =
        todoDataSource.getTodoListOfDate(date).map { it.convertTodoModel() }

    override suspend fun getCompleteTodoListOfDate(date: String): List<TodoModel> =
        todoDataSource.getCompleteTodoListOfDate(date).map { it.convertTodoModel() }

    override suspend fun addTodoModel(todoModel: TodoModel): Result<Unit> = try {
        todoDataSource.addTodoEntity(todoModel.convertTodoEntity())
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(FailedState.FailedAdd)
    }

    override suspend fun deleteTodoModel(todoModel: TodoModel): Result<Unit> = try {
        todoDataSource.deleteTodoEntity(todoModel.convertTodoEntity())
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(FailedState.FailedDelete)
    }

    override suspend fun updateTodoList(todoModel: TodoModel): Result<Unit> = try {
        todoDataSource.updateTodoList(todoModel.convertTodoEntity())
        Result.success(Unit)
    } catch(t: Throwable) {
        Result.failure(FailedState.FailedUpdate)
    }
}