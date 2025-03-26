package com.example.dailylife.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.core.event.Event
import com.example.core.viewmodel.BaseViewModel
import com.example.dailylife.state.TodoDatePickerState
import com.example.data.entitiy.TodoEntity
import com.example.data.mapper.convertTodoEntity
import com.example.data.mapper.convertTodoModel
import com.example.domain.usecase.AddTodoUseCase
import com.example.domain.usecase.DeleteTodoUseCase
import com.example.domain.usecase.GetFutureTodoListUseCase
import com.example.domain.usecase.GetPrevTodoListUseCase
import com.example.domain.usecase.GetTodayCompleteTodoListUseCase
import com.example.domain.usecase.GetTodayTodoListUseCase
import com.example.domain.usecase.UpdateTodoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodayTodoListUseCase: GetTodayTodoListUseCase,
    private val getFutureTodoListUseCase: GetFutureTodoListUseCase,
    private val getTodayCompleteTodoListUseCase: GetTodayCompleteTodoListUseCase,
    private val getPrevTodoListUseCase: GetPrevTodoListUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase
): BaseViewModel() {
    private val _todayTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todayTodoList: StateFlow<List<TodoEntity>> get() = _todayTodoList.asStateFlow()

    private val _futureTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val futureTodoList: StateFlow<List<TodoEntity>> get() = _futureTodoList.asStateFlow()

    private val _todayCompleteTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todayCompleteTodoList: StateFlow<List<TodoEntity>> get() = _todayCompleteTodoList.asStateFlow()

    private val _prevTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val prevTodoList: StateFlow<List<TodoEntity>> get() = _prevTodoList.asStateFlow()

    private val _todoDialogState = MutableStateFlow(TodoDatePickerState())
    val todoDialogState: StateFlow<TodoDatePickerState> get() = _todoDialogState.asStateFlow()

    private val _todoItemContinuationError = MutableSharedFlow<Throwable>()
    val todoItemContinuationError: SharedFlow<Throwable> get() = _todoItemContinuationError.asSharedFlow()

    private val _selectedDate = MutableSharedFlow<String>()
    val selectedDate: SharedFlow<String> get() = _selectedDate.asSharedFlow()

    init {
        publishEvent(Event.NeedRefresh)
    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.NeedRefresh -> refreshTodoList()
        }
    }

    private fun getTodayTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            _todayTodoList.update {
                getTodayTodoListUseCase().map { it.convertTodoEntity() }
            }
        }
    }

    private fun getFutureTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            _futureTodoList.update {
                getFutureTodoListUseCase()
                    .map { it.convertTodoEntity() }
                    .sortedBy { it.dueDate }
            }
        }
    }

    private fun getTodayCompleteTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            _todayCompleteTodoList.update {
                getTodayCompleteTodoListUseCase().map { it.convertTodoEntity() }
            }
        }
    }

    private fun getPrevTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            _prevTodoList.update {
                getPrevTodoListUseCase().map { it.convertTodoEntity() }
            }
        }
    }

    fun addTodoList(todoItem: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                addTodoUseCase(todoItem.convertTodoModel()).onFailure {
                    _todoItemContinuationError.emit(it)
                }
            }

            publishEvent(Event.NeedRefresh)
        }
    }

    fun deleteTodoList(todoItem: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                deleteTodoUseCase(todoItem.convertTodoModel()).onFailure {
                    _todoItemContinuationError.emit(it)
                }
            }

            publishEvent(Event.NeedRefresh)
        }
    }

    fun updateTodoList(todoItem: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                updateTodoListUseCase(todoItem.convertTodoModel()).onFailure {
                    _todoItemContinuationError.emit(it)
                }
            }

            publishEvent(Event.NeedRefresh)
        }
    }

    private fun refreshTodoList() {
        getTodayTodoList()
        getFutureTodoList()
        getTodayCompleteTodoList()
        getPrevTodoList()
    }

    fun showTodoDateDialog() {
        _todoDialogState.update { dialogState ->
            dialogState.copy(isShowDialog = true)
        }
    }

    fun hiddenTodoDateDialog() {
        _todoDialogState.update { dialogState ->
            dialogState.copy(
                isShowDialog = false
            )
        }
    }

    fun updateTodoDate(dueDate: String) {
        _todoDialogState.update { dialogState ->
            dialogState.copy(selectedDate = dueDate)
        }
    }

    fun setSelectedDate(date: String) {
        viewModelScope.launch {
            _selectedDate.emit(date)
        }
    }
}