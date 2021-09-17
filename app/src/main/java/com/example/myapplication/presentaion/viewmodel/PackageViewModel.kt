package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.utils.ViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *  ViewModel для Activity с пакетами
 *  @param iWordRepository - репозиторий для загрузки пакета по id с сервера
 */
class PackageViewModel @Inject constructor(private val iWordRepository: IWordRepository) :
    ViewModel() {

    private val _wordPackageLiveData: MutableLiveData<WordPackage> =
        MutableLiveData<WordPackage>()
    val wordPackageLiveData: LiveData<WordPackage>
        get() = _wordPackageLiveData
    private val _loadViewStateLiveData = MutableLiveData<ViewState>()
    val loadViewStateLiveData: LiveData<ViewState>
        get() = _loadViewStateLiveData
    private val _insertViewStateLiveData = MutableLiveData<ViewState>()
    val insertViewStateLiveData: LiveData<ViewState>
        get() = _insertViewStateLiveData

    /**
     * Загружает нужный пакет с базы данных
     *  @param packageId - параметр для доступа к пакету через базу данных
     */
    fun loadPackage(packageId: Long) {
        viewModelScope.launch {
            try {
                _loadViewStateLiveData.postValue(ViewState.LOADING)
                if (packageId == -1L) {
                    throw RuntimeException("id is not correct!")
                }
                iWordRepository.getPackageWithWordsById(packageId)?.collect {
                    _wordPackageLiveData.postValue(it)
                    _loadViewStateLiveData.postValue(ViewState.LOADED)
                } ?: throw RuntimeException("Cannot load from database")
            } catch (ex: Exception) {
                _loadViewStateLiveData.postValue(ViewState.ERROR)
            }
        }
    }

    /**
     * Создает новую карточку в данном пакете по указанным параметрам
     *  @param frontWord - слово с передней стороны на новой карточке
     *  @param backWord - слово с задней стороны на новой карточке
     */
    fun addNewCardToPackage(frontWord: String, backWord: String) {
        viewModelScope.launch {
            try {
                _insertViewStateLiveData.postValue(ViewState.LOADING)
                _wordPackageLiveData.value?.let { wordPackage ->
                    val result = iWordRepository.addWordPair(frontWord, backWord, wordPackage.id)
                    if (!result) {
                        throw RuntimeException("Error with database!")
                    }
                    _insertViewStateLiveData.postValue(ViewState.LOADED)
                }
            } catch (ex: Exception) {
                _insertViewStateLiveData.postValue(ViewState.ERROR)
            }
        }
    }
}