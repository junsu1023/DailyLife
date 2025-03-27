package com.example.dailylife.viewmodel

import com.example.core.event.Event
import com.example.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(

): BaseViewModel() {
    override fun handleEvent(event: Event) {
        TODO("Not yet implemented")
    }

}