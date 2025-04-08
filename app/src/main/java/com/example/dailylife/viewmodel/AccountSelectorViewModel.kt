package com.example.dailylife.viewmodel

import com.example.core.event.Event
import com.example.core.viewmodel.BaseViewModel
import com.example.dailylife.util.onDefault
import com.example.dailylife.util.onIO
import com.example.data.entitiy.CardCompanyEntity
import com.example.data.entitiy.ClassificationEntity
import com.example.data.mapper.convertEntity
import com.example.data.mapper.convertModel
import com.example.domain.usecase.cardcompany.AddCardCompanyUseCase
import com.example.domain.usecase.cardcompany.DeleteCardCompanyUseCase
import com.example.domain.usecase.cardcompany.GetCardCompanyListUseCase
import com.example.domain.usecase.cardcompany.UpdateCardCompanyUseCase
import com.example.domain.usecase.classification.AddClassificationUseCase
import com.example.domain.usecase.classification.DeleteClassificationUseCase
import com.example.domain.usecase.classification.GetClassificationListUseCase
import com.example.domain.usecase.classification.UpdateClassificationUseCase
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
class AccountSelectorViewModel @Inject constructor(
    private val getClassificationListUseCase: GetClassificationListUseCase,
    private val addClassificationUseCase: AddClassificationUseCase,
    private val deleteClassificationUseCase: DeleteClassificationUseCase,
    private val updateClassificationUseCase: UpdateClassificationUseCase,
    private val getCardCompanyListUseCase: GetCardCompanyListUseCase,
    private val addCardCompanyUseCase: AddCardCompanyUseCase,
    private val deleteCardCompanyUseCase: DeleteCardCompanyUseCase,
    private val updateCardCompanyUseCase: UpdateCardCompanyUseCase
): BaseViewModel() {
    private val _classificationList = MutableStateFlow<List<ClassificationEntity>>(emptyList())
    val classificationList: StateFlow<List<ClassificationEntity>> get() = _classificationList.asStateFlow()

    private val _cardCompanyList = MutableStateFlow<List<CardCompanyEntity>>(emptyList())
    val cardCompanyList: StateFlow<List<CardCompanyEntity>> get() = _cardCompanyList.asStateFlow()

    private val _chunkedClassificationList = MutableStateFlow<List<List<ClassificationEntity>>>(emptyList())
    val chunkedClassificationList: StateFlow<List<List<ClassificationEntity>>> get() = _chunkedClassificationList.asStateFlow()

    private val _chunkedCardCompanyList = MutableStateFlow<List<List<CardCompanyEntity>>>(emptyList())
    val chunkedCardCompanyList: StateFlow<List<List<CardCompanyEntity>>> get() = _chunkedCardCompanyList.asStateFlow()

    private val _editContinuationError = MutableSharedFlow<Throwable>()
    val editContinuationError: SharedFlow<Throwable> get() = _editContinuationError.asSharedFlow()

    private val _selectedClassification = MutableSharedFlow<String>()
    val selectedClassification: SharedFlow<String> get() = _selectedClassification.asSharedFlow()

    private val _selectedCardCompany = MutableSharedFlow<String>()
    val selectedCardCompany: SharedFlow<String> get() = _selectedCardCompany.asSharedFlow()

    init {
        getClassificationList()
        getCardCompanyList()
    }

    override fun handleEvent(event: Event) {
        when(event) {
            Event.NeedRefresh -> refresh()
            Event.Chunked -> chunkedList()
            else -> { }
        }
    }

    private fun getClassificationList() = onIO {
        _classificationList.update {
            getClassificationListUseCase().map { it.convertEntity() }.toMutableList()
        }

        publishEvent(Event.Chunked)
    }

    private fun getCardCompanyList() = onIO {
        _cardCompanyList.update {
            getCardCompanyListUseCase().map { it.convertEntity() }.toMutableList()
        }

        publishEvent(Event.Chunked)
    }

    fun addClassification(item: ClassificationEntity) = onIO {
        addClassificationUseCase(item.convertModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
        }.onFailure {
            _editContinuationError.emit(it)
        }
    }

    fun addCardCompany(item: CardCompanyEntity) = onIO {
        addCardCompanyUseCase(item.convertModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
        }.onFailure {
            _editContinuationError.emit(it)
        }
    }

    fun deleteClassification(item: ClassificationEntity) = onIO {
        deleteClassificationUseCase(item.convertModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
        }.onFailure {
            _editContinuationError.emit(it)
        }
    }

    fun deleteCardCompany(item: CardCompanyEntity) = onIO {
        deleteCardCompanyUseCase(item.convertModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
        }.onFailure {
            _editContinuationError.emit(it)
        }
    }

    fun updateClassification(item: ClassificationEntity) = onIO {
        updateClassificationUseCase(item.convertModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
        }.onFailure {
            _editContinuationError.emit(it)
        }
    }

    fun updateCardCompany(item: CardCompanyEntity) = onIO {
        updateCardCompanyUseCase(item.convertModel()).onSuccess {
            publishEvent(Event.NeedRefresh)
        }.onFailure {
            _editContinuationError.emit(it)
        }
    }

    private fun chunkedClassificationList() = onDefault {
        _chunkedClassificationList.update {
            _classificationList.value.chunked(4)
        }
    }

    private fun chunkedCardCompanyList() = onDefault {
        _chunkedCardCompanyList.update {
            _cardCompanyList.value.chunked(4)
        }
    }

    private fun chunkedList() {
        chunkedClassificationList()
        chunkedCardCompanyList()
    }

    private fun refresh() {
        getClassificationList()
        getCardCompanyList()
    }

    fun setSelectedClassification(classification: String) = onDefault {
        _selectedClassification.emit(classification)
    }

    fun setSelectedCardCompany(cardCompany: String) = onDefault {
        _selectedCardCompany.emit(cardCompany)
    }
}