package com.example.dailylife.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.dailylife.util.SingleLiveEvent
import com.example.data.entitiy.TodoEntity
import com.example.data.mapper.convertTodoEntity
import com.example.data.mapper.convertTodoModel
import com.example.domain.usecase.AddTodoUseCase
import com.example.domain.usecase.DeleteTodoUseCase
import com.example.domain.usecase.GetFutureTodoListUseCase
import com.example.domain.usecase.GetTodayTodoListUseCase
import com.example.domain.usecase.GetTodoListUseCase
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getAllTodoListUseCase: GetTodoListUseCase,
    private val getTodayTodoListUseCase: GetTodayTodoListUseCase,
    private val getFutureTodoListUseCase: GetFutureTodoListUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
): ViewModel() {
    private val _curDate = MutableSharedFlow<String>()
    val curDate: SharedFlow<String> get() = _curDate.asSharedFlow()

    private val _todoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todoList: StateFlow<List<TodoEntity>> get() = _todoList.asStateFlow()

    private val _todayTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todayTodoList: StateFlow<List<TodoEntity>> get() = _todayTodoList.asStateFlow()

    private val _futureTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val futureTodoList: StateFlow<List<TodoEntity>> get() = _futureTodoList.asStateFlow()

    private val _todayCompleteTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todayCompleteTodoList: StateFlow<List<TodoEntity>> get() = _todayCompleteTodoList.asStateFlow()

    private val _addTodoItemContinuationError = SingleLiveEvent<Throwable>()
    val addTodoItemContinuationError: LiveData<Throwable> get() = _addTodoItemContinuationError

    private val _deleteTodoItemContinuationError = SingleLiveEvent<Throwable>()
    val deleteTodoItemContinuationError: LiveData<Throwable> get() = _deleteTodoItemContinuationError

    init {
        getDate()
        getTodoList()
        getTodayTodoList()
        getFutureTodoList()
    }

    fun getDate() {
        viewModelScope.launch {
            _curDate.emit(LocalDate.now().toString())
        }
    }

    fun getTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            _todoList.update {
                getAllTodoListUseCase().map { it.convertTodoEntity() }
            }
        }
    }

    fun getTodayTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            _todayTodoList.update {
                getTodayTodoListUseCase().map { it.convertTodoEntity() }
            }
        }
    }

    fun getFutureTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            _futureTodoList.update {
                getFutureTodoListUseCase().map { it.convertTodoEntity()}
            }
        }
    }

    fun addTodoList(todoItem: TodoEntity) {
        viewModelScope.launch {
            addTodoUseCase(todoItem.convertTodoModel()).onSuccess {
                val todoItemList = _todoList.value.toMutableList()
                todoItemList.remove(todoItem)

                _todoList.update { todoItemList }
            }.onFailure {
                _addTodoItemContinuationError.value = it
            }
        }
    }

    fun deleteTodoList(todoItem: TodoEntity) {
        viewModelScope.launch {
            deleteTodoUseCase(todoItem.convertTodoModel()).onSuccess {
                val todoItemList = _todoList.value.toMutableList()
                todoItemList.remove(todoItem)

                _todoList.update { todoItemList }
            }.onFailure {
                _deleteTodoItemContinuationError.value = it
            }
        }
    }
}