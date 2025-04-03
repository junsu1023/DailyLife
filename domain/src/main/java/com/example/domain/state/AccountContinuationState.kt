package com.example.domain.state

sealed class AccountContinuationState: Throwable() {
    data object InValidDateFormat: AccountContinuationState() {
        private fun readResolve(): Any = InValidDateFormat
    }

    data object InvalidCostFormat: AccountContinuationState() {
        private fun readResolve(): Any = InvalidCostFormat
    }
}