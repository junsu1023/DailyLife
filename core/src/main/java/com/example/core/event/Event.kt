package com.example.core.event

sealed class Event {
    data object NeedRefresh: Event()
    data object NeedGroup: Event()
}