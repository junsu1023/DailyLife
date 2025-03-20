package com.example.domain.usecase

import com.example.domain.model.TodoModel
import com.example.domain.repository.TodoRepository
import javax.inject.Inject

class UpdateTodoListUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(todoModel: TodoModel): Result<Unit> = try {
        todoRepository.updateTodoList(todoModel)
        Result.success(Unit)
    } catch (t: Throwable) {
        Result.failure(t)
    }
}