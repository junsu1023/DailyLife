package com.example.dailylife.viewmodel

import com.example.core.event.Event
import com.example.core.viewmodel.BaseViewModel
import com.example.dailylife.state.TodoDatePickerState
import com.example.dailylife.util.getToday
import com.example.dailylife.util.onDefault
import com.example.dailylife.util.onIO
import com.example.data.entitiy.TodoEntity
import com.example.data.mapper.convertTodoEntity
import com.example.data.mapper.convertTodoModel
import com.example.domain.usecase.todo.AddTodoUseCase
import com.example.domain.usecase.todo.DeleteTodoUseCase
import com.example.domain.usecase.todo.GetAllTodoListUseCase
import com.example.domain.usecase.todo.GetCompleteTodoListOfDateUseCase
import com.example.domain.usecase.todo.GetTodoListOfFutureUseCase
import com.example.domain.usecase.todo.GetTodoListOfPrevUseCase
import com.example.domain.usecase.todo.GetCompleteTodoListOfTodayUseCase
import com.example.domain.usecase.todo.GetTodoListOfDateUseCase
import com.example.domain.usecase.todo.GetTodoListOfTodayUseCase
import com.example.domain.usecase.todo.UpdateTodoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getAllTodoListUseCase: GetAllTodoListUseCase,
    private val getTodoListOfTodayUseCase: GetTodoListOfTodayUseCase,
    private val getTodoListOfFutureUseCase: GetTodoListOfFutureUseCase,
    private val getCompleteTodoListOfTodayUseCase: GetCompleteTodoListOfTodayUseCase,
    private val getTodoListOfPrevUseCase: GetTodoListOfPrevUseCase,
    private val getTodoListOfDateUseCase: GetTodoListOfDateUseCase,
    private val getCompleteTodoListOfDateUseCase: GetCompleteTodoListOfDateUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase
): BaseViewModel() {
    private val _allTodoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    val allTodoList: StateFlow<List<TodoEntity>> get() = _allTodoList.asStateFlow()

    private val _todoListOfToday = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todoListOfToday: StateFlow<List<TodoEntity>> get() = _todoListOfToday.asStateFlow()

    private val _todoListOfFuture = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todoListOfFuture: StateFlow<List<TodoEntity>> get() = _todoListOfFuture.asStateFlow()

    private val _completeTodoListOfToday = MutableStateFlow<List<TodoEntity>>(emptyList())
    val completeTodoListOfToday: StateFlow<List<TodoEntity>> get() = _completeTodoListOfToday.asStateFlow()

    private val _todoListOfPrev = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todoListOfPrev: StateFlow<List<TodoEntity>> get() = _todoListOfPrev.asStateFlow()

    private val _todoListOfDate = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todoListOfDate: StateFlow<List<TodoEntity>> get() = _todoListOfDate.asStateFlow()

    private val _completeTodoListOfDate = MutableStateFlow<List<TodoEntity>>(emptyList())
    val completeTodoListOfDate: StateFlow<List<TodoEntity>> get() = _completeTodoListOfDate.asStateFlow()

    private val _todoDialogState = MutableStateFlow(TodoDatePickerState())
    val todoDialogState: StateFlow<TodoDatePickerState> get() = _todoDialogState.asStateFlow()

    private val _todoItemContinuationError = MutableSharedFlow<Throwable>()
    val todoItemContinuationError: SharedFlow<Throwable> get() = _todoItemContinuationError.asSharedFlow()

    private val _selectedDate = MutableStateFlow(getToday())
    val selectedDate: StateFlow<String> get() = _selectedDate.asStateFlow()

    init {
        publishEvent(Event.NeedRefresh)
    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.NeedRefresh -> refreshTodoList()
            else -> { }
        }
    }

    private fun getAllTodoList() = onIO {
        _allTodoList.update {
            getAllTodoListUseCase().map { it.convertTodoEntity() }
        }
    }

    private fun getTodayTodoList() = onIO {
        _todoListOfToday.update {
            getTodoListOfTodayUseCase().map { it.convertTodoEntity() }
        }
    }

    private fun getFutureTodoList() = onIO {
        _todoListOfFuture.update {
            getTodoListOfFutureUseCase()
                .map { it.convertTodoEntity() }
                .sortedBy { it.dueDate }
        }
    }

    private fun getTodayCompleteTodoList() = onIO {
        _completeTodoListOfToday.update {
            getCompleteTodoListOfTodayUseCase().map { it.convertTodoEntity() }
        }
    }

    private fun getPrevTodoList() = onIO {
        _todoListOfPrev.update {
            getTodoListOfPrevUseCase().map { it.convertTodoEntity() }
        }
    }

    private fun getTodoListOfDate() = onIO {
        _todoListOfDate.update {
            getTodoListOfDateUseCase(_selectedDate.value).map { it.convertTodoEntity() }
        }
    }

    private fun getCompleteTodoListOfDate() = onIO {
        _completeTodoListOfDate.update {
            getCompleteTodoListOfDateUseCase(_selectedDate.value).map { it.convertTodoEntity() }
        }
    }

    fun addTodoList(todoItem: TodoEntity) = onIO {
        addTodoUseCase(todoItem.convertTodoModel()).onFailure {
            _todoItemContinuationError.emit(it)
        }

        publishEvent(Event.NeedRefresh)
    }

    fun deleteTodoList(todoItem: TodoEntity) = onIO {
        deleteTodoUseCase(todoItem.convertTodoModel()).onFailure {
            _todoItemContinuationError.emit(it)
        }

        publishEvent(Event.NeedRefresh)
    }

    fun updateTodoList(todoItem: TodoEntity) = onIO {
        updateTodoListUseCase(todoItem.convertTodoModel()).onFailure {
            _todoItemContinuationError.emit(it)
        }

        publishEvent(Event.NeedRefresh)
    }

    private fun refreshTodoList() {
        getAllTodoList()
        getTodayTodoList()
        getFutureTodoList()
        getTodayCompleteTodoList()
        getPrevTodoList()
        getTodoListOfDate()
        getCompleteTodoListOfDate()
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

    fun updateTodoDate(dueDate: String) = onDefault {
        _todoDialogState.update { dialogState ->
            dialogState.copy(selectedDate = dueDate)
        }
    }

    fun setSelectedDate(date: String) = onDefault {
        _selectedDate.update { date }
    }
}