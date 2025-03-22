package com.example.dailylife.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dailylife.util.BaseViewModel
import com.example.dailylife.util.SingleLiveEvent
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
): BaseViewModel() {
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

    private val _updateTodoListContinuationError = SingleLiveEvent<Throwable>()
    val updateTodoListContinuationError: LiveData<Throwable> get() = _updateTodoListContinuationError

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
                    _addTodoItemContinuationError.value = it
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
                    _deleteTodoItemContinuationError.value = it
                }
            }.join()

            refreshTodoList()
        }
    }

    fun updateTodoList(todoItem: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                updateTodoListUseCase(todoItem.convertTodoModel()).onSuccess {
                    // 동작 X
                }.onFailure {
                    _updateTodoListContinuationError.value = it
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
}