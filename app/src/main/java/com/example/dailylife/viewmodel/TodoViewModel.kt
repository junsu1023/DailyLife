package com.example.dailylife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailylife.state.TodoDatePickerState
import com.example.data.entitiy.TodoEntity
import com.example.data.mapper.convertTodoEntity
import com.example.data.mapper.convertTodoModel
import com.example.domain.usecase.AddTodoUseCase
import com.example.domain.usecase.DeleteTodoUseCase
import com.example.domain.usecase.GetFutureTodoListUseCase
import com.example.domain.usecase.GetTodayCompleteTodoListUseCase
import com.example.domain.usecase.GetTodayTodoListUseCase
import com.example.domain.usecase.GetTodoListUseCase
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getAllTodoListUseCase: GetTodoListUseCase,
    private val getTodayTodoListUseCase: GetTodayTodoListUseCase,
    private val getFutureTodoListUseCase: GetFutureTodoListUseCase,
    private val getTodayCompleteTodoListUseCase: GetTodayCompleteTodoListUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase
): ViewModel() {
    private val _todoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todoList: StateFlow<List<TodoEntity>> get() = _todoList.asStateFlow()

    private val _todayTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todayTodoList: StateFlow<List<TodoEntity>> get() = _todayTodoList.asStateFlow()

    private val _futureTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val futureTodoList: StateFlow<List<TodoEntity>> get() = _futureTodoList.asStateFlow()

    private val _todayCompleteTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todayCompleteTodoList: StateFlow<List<TodoEntity>> get() = _todayCompleteTodoList.asStateFlow()

    private val _addTodoItemContinuationError = MutableSharedFlow<Throwable>()
    val addTodoItemContinuationError: SharedFlow<Throwable> get() = _addTodoItemContinuationError.asSharedFlow()

    private val _deleteTodoItemContinuationError = MutableSharedFlow<Throwable>()
    val deleteTodoItemContinuationError: SharedFlow<Throwable> get() = _deleteTodoItemContinuationError.asSharedFlow()

    private val _updateTodoItemContinuationError = MutableSharedFlow<Throwable>()
    val updateTodoListContinuationError: SharedFlow<Throwable> get() = _updateTodoItemContinuationError.asSharedFlow()

    private val _todoDialogState = MutableStateFlow(TodoDatePickerState())
    val todoDialogState: StateFlow<TodoDatePickerState> get() = _todoDialogState.asStateFlow()

    private val _selectedDate = MutableSharedFlow<String>()
    val selectedDate: SharedFlow<String> get() = _selectedDate

    init {
        viewModelScope.launch(Dispatchers.IO) {
            refreshTodoList()
        }
    }

    private suspend fun getTodoList() {
        withContext(Dispatchers.IO) {
            _todoList.update {
                getAllTodoListUseCase().map { it.convertTodoEntity() }
            }
        }
    }

    private suspend fun getTodayTodoList() {
        withContext(Dispatchers.IO) {
            _todayTodoList.update {
                getTodayTodoListUseCase().map { it.convertTodoEntity() }
            }
        }
    }

    private suspend fun getFutureTodoList() {
        withContext(Dispatchers.IO) {
            _futureTodoList.update {
                getFutureTodoListUseCase().map { it.convertTodoEntity()}
            }
        }
    }

    private suspend fun getTodayCompleteTodoList() {
        withContext(Dispatchers.IO) {
            _todayCompleteTodoList.update {
                getTodayCompleteTodoListUseCase().map { it.convertTodoEntity() }
            }
        }
    }

    fun addTodoList(todoItem: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                addTodoUseCase(todoItem.convertTodoModel()).onSuccess {
                    val todoItemList = _todoList.value.toMutableList()
                    todoItemList.remove(todoItem)
                    _todoList.update { todoItemList }
                }.onFailure {
                    _addTodoItemContinuationError.emit(it)
                }
            }.join()

            refreshTodoList()
        }
    }

    fun deleteTodoList(todoItem: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                deleteTodoUseCase(todoItem.convertTodoModel()).onSuccess {
                    val todoItemList = _todoList.value.toMutableList()
                    todoItemList.remove(todoItem)

                    _todoList.update { todoItemList }
                }.onFailure {
                    _deleteTodoItemContinuationError.emit(it)
                }
            }.join()

            refreshTodoList()
        }
    }

    fun updateTodoList(todoItem: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                updateTodoListUseCase(todoItem.convertTodoModel()).onSuccess {

                }.onFailure {
                    _updateTodoItemContinuationError.emit(it)
                }
            }.join()

            refreshTodoList()
        }
    }

    suspend fun refreshTodoList() {
        getTodoList()
        getTodayTodoList()
        getFutureTodoList()
        getTodayCompleteTodoList()
    }

    fun showTodoDateDialog() {
        _todoDialogState.update { dialogState ->
            dialogState.copy(isShowDialog = true)
        }
    }

    fun hiddenTodoDateDialog() {
        _todoDialogState.update { dialogState ->
            dialogState.copy(isShowDialog = false)
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