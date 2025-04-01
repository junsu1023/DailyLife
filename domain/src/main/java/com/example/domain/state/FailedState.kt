package com.example.domain.state

sealed class FailedState: Throwable() {
    data object FailedAdd: FailedState() {
        private fun readResolve(): Any = FailedAdd
    }

    data object FailedDelete: FailedState() {
        private fun readResolve(): Any = FailedDelete
    }

    data object FailedUpdate: FailedState() {
        private fun readResolve(): Any = FailedUpdate
    }
}