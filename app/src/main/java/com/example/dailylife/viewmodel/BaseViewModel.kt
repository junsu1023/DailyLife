package com.example.dailylife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.event.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {
    abstract fun handleEvent(event: Event)

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    init {
        subscribeEvent()
    }

    private fun subscribeEvent() {
        viewModelScope.launch {
            _event.collectLatest {
                handleEvent(it)
            }
        }
    }

    fun publishEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
}