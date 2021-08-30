package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.data.repository.IWordRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class MainViewModel(private val roomRepository: IWordRepository) : ViewModel() {
    private val _wordPackageListLiveData = MutableLiveData<List<WordPackage>>()
    val wordPackageListLiveData: LiveData<List<WordPackage>>
        get() = _wordPackageListLiveData
    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean>
        get() = _isLoadingLiveData
    private val _exceptionLiveData = MutableLiveData<Exception>()
    val exceptionLiveData: LiveData<Exception>
        get() = _exceptionLiveData


    fun loadWordPackagesFromDatabase() {
        viewModelScope.launch {
            try {
                val list = roomRepository.getAllPackages()
                list?.collect { flowList ->
                    _isLoadingLiveData.postValue(true)
                    _wordPackageListLiveData.postValue(flowList)
                    _isLoadingLiveData.postValue(false)
                } ?: throw NullPointerException("Cannot load from database because null variable!")
            } catch (ex: Exception) {
                _isLoadingLiveData.postValue(false)
                _exceptionLiveData.postValue(ex)
            }
        }
    }

    fun insertWordPackage(wordPackage: WordPackage) {
        viewModelScope.launch {
            try {
                val resultInsert = roomRepository.addPackage(wordPackage)
                if (!resultInsert) throw NullPointerException("Cannot insert in database because null variable!")
            } catch (ex: Exception) {

            }
        }
    }

    class MainFactory(
        private val iWordRepository: IWordRepository,
    ) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(iWordRepository) as T
        }
    }
}