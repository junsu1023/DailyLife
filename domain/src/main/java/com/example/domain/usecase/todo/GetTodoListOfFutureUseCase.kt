package com.example.domain.usecase.todo

import com.example.domain.model.TodoModel
import com.example.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoListOfFutureUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(): List<TodoModel> = todoRepository.getTodoListOfFuture()
}