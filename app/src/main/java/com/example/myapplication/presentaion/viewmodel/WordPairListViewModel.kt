package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.model.WordPackageInfoModel
import com.example.myapplication.presentaion.utils.ViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/**
 *  фрагмент, показывающий список карточек пакета
 *  @param iWordRepository - репозиторий для доступа к базе данных
 *  @property wordListPackage - LiveData, хранящая пакет с карточками
 *  @property loadViewStateLiveData - LiveData, хранящая состояние View
 */
class WordPairListViewModel @Inject constructor(
    private val iWordRepository: IWordRepository,
    wordPackageInfoModel: WordPackageInfoModel
) : ViewModel() {

    private val _WordListPackage: MutableLiveData<WordPackage> =
        MutableLiveData<WordPackage>()
    val wordListPackage: LiveData<WordPackage>
        get() = _WordListPackage

    private val _loadViewStateLiveData = MutableLiveData<ViewState>()
    val loadViewStateLiveData: LiveData<ViewState>
        get() = _loadViewStateLiveData

    init {
        updateList(wordPackageInfoModel.id)
    }


    fun updateList(packageId: Long) {
        viewModelScope.launch {
            try {
                _loadViewStateLiveData.postValue(ViewState.LOADING)
                iWordRepository.getPackageWithWordsById(packageId)?.collect { wordPackage ->
                    _WordListPackage.postValue(wordPackage)
                    _loadViewStateLiveData.postValue(ViewState.LOADED)
                }
            } catch (ex: Exception) {
                _loadViewStateLiveData.postValue(ViewState.ERROR)
            }
        }
    }

}