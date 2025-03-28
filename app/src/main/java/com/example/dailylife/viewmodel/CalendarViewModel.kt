package com.example.dailylife.viewmodel

import com.example.core.event.Event
import com.example.core.viewmodel.BaseViewModel
import com.example.dailylife.util.onIO
import com.example.data.entitiy.TodoEntity
import com.example.domain.usecase.GetDateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getDateTodoUseCase: GetDateTodoUseCase
): BaseViewModel() {
    private val _todoListOfDate = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todoListOfDate: StateFlow<List<TodoEntity>> get() = _todoListOfDate.asStateFlow()

    init {

    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.NeedRefresh -> refreshTodoList()
            else -> { }
        }
    }

    fun getTodoListOfDate(date: String) = onIO {

    }

    fun refreshTodoList() {

    }
}