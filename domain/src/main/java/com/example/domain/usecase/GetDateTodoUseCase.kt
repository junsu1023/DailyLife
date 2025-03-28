package com.example.domain.usecase

import com.example.domain.model.TodoModel
import com.example.domain.repository.CalendarRepository
import javax.inject.Inject

class GetDateTodoUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(date: String): List<TodoModel> = calendarRepository.getTodoListOfDate(date)
}