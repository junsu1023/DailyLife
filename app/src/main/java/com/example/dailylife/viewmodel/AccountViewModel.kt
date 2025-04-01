package com.example.dailylife.viewmodel

import com.example.core.event.Event
import com.example.core.viewmodel.BaseViewModel
import com.example.dailylife.util.convertString
import com.example.dailylife.util.onDefault
import com.example.dailylife.util.onIO
import com.example.data.entitiy.AccountEntity
import com.example.data.mapper.convertAccountItemEntity
import com.example.data.mapper.convertAccountItemModel
import com.example.domain.usecase.account.AddAccountItemUseCase
import com.example.domain.usecase.account.DeleteAccountItemUseCase
import com.example.domain.usecase.account.GetCurrentYMAccountInfoUseCase
import com.example.domain.usecase.account.UpdateAccountItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentYMAccountInfoUseCase: GetCurrentYMAccountInfoUseCase,
    private val addAccountItemUseCase: AddAccountItemUseCase,
    private val deleteAccountItemUseCase: DeleteAccountItemUseCase,
    private val updateAccountItemUseCase: UpdateAccountItemUseCase
): BaseViewModel() {
    private val _currentYM = MutableStateFlow<YearMonth>(YearMonth.from(LocalDate.now()))
    val currentYM: StateFlow<YearMonth> get() = _currentYM.asStateFlow()

    private val _currentYMAccountList = MutableStateFlow<List<AccountEntity>>(emptyList())
    val currentYMAccountList: StateFlow<List<AccountEntity>> get() = _currentYMAccountList.asStateFlow()

    private val _accountContinuationError = MutableSharedFlow<Throwable>()
    val accountContinuationError: SharedFlow<Throwable> get() = _accountContinuationError.asSharedFlow()

    init {
        publishEvent(Event.NeedRefresh)
    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.NeedRefresh -> refresh()
            else -> { /* NONE */ }
        }
    }

    fun increaseCurrentYM() = onDefault {
        _currentYM.update {
            _currentYM.value.plusMonths(1)
        }
    }

    fun decreaseCurrentYM() = onDefault {
        _currentYM.update {
            _currentYM.value.plusMonths(-1)
        }
    }

    fun getCurrentMonthAccountInfo() = onIO {
        _currentYMAccountList.update {
            getCurrentYMAccountInfoUseCase(_currentYM.value.convertString()).map { it.convertAccountItemEntity() }
        }
    }

    fun addAccountItem(accountItem: AccountEntity) = onIO {
        addAccountItemUseCase(accountItem.convertAccountItemModel()).onFailure {
            _accountContinuationError.emit(it)
        }

        publishEvent(Event.NeedRefresh)
    }

    fun deleteAccountItem(accountItem: AccountEntity) = onIO {
        deleteAccountItemUseCase(accountItem.convertAccountItemModel()).onFailure {
            _accountContinuationError.emit(it)
        }

        publishEvent(Event.NeedRefresh)
    }

    fun updateAccountItem(accountItem: AccountEntity) = onIO {
        updateAccountItemUseCase(accountItem.convertAccountItemModel()).onFailure {
            _accountContinuationError.emit(it)
        }

        publishEvent(Event.NeedRefresh)
    }

    fun refresh() {
        getCurrentMonthAccountInfo()
    }
}