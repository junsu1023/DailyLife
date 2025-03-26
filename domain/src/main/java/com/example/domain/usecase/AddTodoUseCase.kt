package com.example.domain.usecase

import com.example.domain.model.TodoModel
import com.example.domain.repository.TodoRepository
import com.example.domain.state.TodoFailedState
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(todoModel: TodoModel): Result<Unit> =
        try {
            todoRepository.addTodoModel(todoModel)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(TodoFailedState.FailedAdd)
        }
}