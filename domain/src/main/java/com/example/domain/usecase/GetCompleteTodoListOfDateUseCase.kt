package com.example.domain.usecase

import com.example.domain.model.TodoModel
import com.example.domain.repository.TodoRepository
import javax.inject.Inject

class GetCompleteTodoListOfDateUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(date: String): List<TodoModel> = todoRepository.getCompleteTodoListOfDate(date)
}