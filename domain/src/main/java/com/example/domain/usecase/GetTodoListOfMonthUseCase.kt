package com.example.domain.usecase

import com.example.domain.model.TodoModel
import com.example.domain.repository.CalendarRepository
import javax.inject.Inject

class GetTodoListOfMonthUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(month: Int): List<TodoModel> = calendarRepository.getTodoListOfMonth(month)
}