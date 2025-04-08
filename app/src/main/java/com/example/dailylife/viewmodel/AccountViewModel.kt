package com.example.dailylife.viewmodel

import com.example.core.event.Event
import com.example.core.viewmodel.BaseViewModel
import com.example.dailylife.util.convertDBString
import com.example.dailylife.util.onDefault
import com.example.dailylife.util.onIO
import com.example.data.entitiy.AccountEntity
import com.example.data.mapper.convertAccountItemEntity
import com.example.data.mapper.convertAccountItemModel
import com.example.domain.usecase.account.AddAccountItemUseCase
import com.example.domain.usecase.account.DeleteAccountItemUseCase
import com.example.domain.usecase.account.GetAllAccountInfoUseCase
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
    private val getAllAccountInfoUseCase: GetAllAccountInfoUseCase,
    private val getCurrentYMAccountInfoUseCase: GetCurrentYMAccountInfoUseCase,
    private val addAccountItemUseCase: AddAccountItemUseCase,
    private val deleteAccountItemUseCase: DeleteAccountItemUseCase,
    private val updateAccountItemUseCase: UpdateAccountItemUseCase
): BaseViewModel() {
    private val _currentYM = MutableStateFlow<YearMonth>(YearMonth.from(LocalDate.now()))
    val currentYM: StateFlow<YearMonth> get() = _currentYM.asStateFlow()

    private val _allAccountList = MutableStateFlow<List<AccountEntity>>(emptyList())
    val allAccountList: StateFlow<List<AccountEntity>> get() = _allAccountList.asStateFlow()

    private val _currentYMAccountList = MutableStateFlow<List<AccountEntity>>(emptyList())
    val currentYMAccountList: StateFlow<List<AccountEntity>> get() = _currentYMAccountList.asStateFlow()

    private val _accountContinuationSuccess = MutableSharedFlow<Unit>()
    val accountContinuationSuccess: SharedFlow<Unit> get() = _accountContinuationSuccess.asSharedFlow()

    private val _accountContinuationError = MutableSharedFlow<Throwable>()
    val accountContinuationError: SharedFlow<Throwable> get() = _accountContinuationError.asSharedFlow()

    private val _accountOfDateGroup = MutableStateFlow<Map<String, List<AccountEntity>>>(emptyMap())
    val accountOfDateGroup: StateFlow<Map<String, List<AccountEntity>>> get() = _accountOfDateGroup.asStateFlow()

    init {
        publishEvent(Event.NeedRefresh)
    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.NeedRefresh -> refresh()
            Event.NeedGroup -> groupSameDate()
            else -> { /* NONE */ }
        }
    }

    private fun getAllAccountList() = onIO {
        _allAccountList.update {
            getAllAccountInfoUseCase().map { it.convertAccountItemEntity() }
        }
    }

    private fun getCurrentMonthAccountInfo() = onIO {
        _currentYMAccountList.update {
            getCurrentYMAccountInfoUseCase(_currentYM.value.convertDBString()).map { it.convertAccountItemEntity() }
        }

        publishEvent(Event.NeedGroup)
    }

    fun addAccountItem(accountItem: AccountEntity) = onIO {
        addAccountItemUseCase(accountItem.convertAccountItemModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
            _accountContinuationSuccess.emit(it)
        }.onFailure {
            _accountContinuationError.emit(it)
        }
    }

    fun deleteAccountItem(accountItem: AccountEntity) = onIO {
        deleteAccountItemUseCase(accountItem.convertAccountItemModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
            _accountContinuationSuccess.emit(it)
        }.onFailure {
            _accountContinuationError.emit(it)
        }
    }

    fun updateAccountItem(accountItem: AccountEntity) = onIO {
        updateAccountItemUseCase(accountItem.convertAccountItemModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
            _accountContinuationSuccess.emit(it)
        }.onFailure {
            _accountContinuationError.emit(it)
        }
    }

    fun increaseCurrentYM() = onDefault {
        _currentYM.update {
            _currentYM.value.plusMonths(1)
        }

        publishEvent(Event.NeedRefresh)
    }

    fun decreaseCurrentYM() = onDefault {
        _currentYM.update {
            _currentYM.value.minusMonths(1)
        }

        publishEvent(Event.NeedRefresh)
    }

    private fun groupSameDate() {
        val map = mutableMapOf<String, List<AccountEntity>>()

        _currentYMAccountList.value.forEach { accountInfo ->
            val date = accountInfo.date
            val dateList = map[date]?.toMutableList() ?: mutableListOf()
            dateList.add(accountInfo)

            map[date] = dateList
        }

        _accountOfDateGroup.update { map }
    }

    fun getTotal(kind: String): Long = _currentYMAccountList.value.filter { it.kind == kind }.sumOf { it.cost }

    private fun refresh() {
        getAllAccountList()
        getCurrentMonthAccountInfo()
    }
}