package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.usecase.UpdateWordPairsUseCase
import com.example.myapplication.presentaion.utils.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  фрагмент, показывающий список карточек пакета
 *  @property wordPackageFlow - Flow, хранящий пакет с карточками
 *  @property loadViewStateFlow - Flow, хранящий состояние View
 */

@ExperimentalCoroutinesApi
class WordPairListViewModel @Inject constructor(
    private val updateWordPairsUseCase: UpdateWordPairsUseCase
) : ViewModel() {

    val wordPackageFlow: StateFlow<WordPackage> =
        updateWordPairsUseCase.subscribeOnWordPackageById().stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = WordPackage.emptyModel()
        )

    val loadViewStateFlow: StateFlow<ViewState> = wordPackageFlow.flatMapLatest {
        flow { emit(ViewState.LOADED) }
    }.catch { emit(ViewState.ERROR) }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = ViewState.LOADING
    )

    fun reversePackage() {
        updateWordPairsUseCase.updateReverse()
    }
}