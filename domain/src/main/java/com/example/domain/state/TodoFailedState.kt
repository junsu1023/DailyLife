package com.example.domain.state

sealed class TodoFailedState: Throwable() {
    data object FailedAdd: TodoFailedState() {
        private fun readResolve(): Any = FailedAdd
    }

    data object FailedDelete: TodoFailedState() {
        private fun readResolve(): Any = FailedDelete
    }

    data object FailedUpdate: TodoFailedState() {
        private fun readResolve(): Any = FailedUpdate
    }
}