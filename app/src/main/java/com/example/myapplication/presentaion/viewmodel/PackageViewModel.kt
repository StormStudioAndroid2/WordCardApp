package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class PackageViewModel(private val iWordRepository: IWordRepository) : ViewModel() {
    private val _wordPackageLiveData: MutableLiveData<WordPackage> =
        MutableLiveData<WordPackage>()

    val wordPackageLiveData: LiveData<WordPackage>
        get() = _wordPackageLiveData

    fun loadPackage(packageId: Long) {
        viewModelScope.launch {
            try {
                iWordRepository.getPackageWithWordsById(packageId)?.collect {
                    _wordPackageLiveData.postValue(it)
                } ?: throw NullPointerException("Null in database")
            } catch (ex: Exception) {

            }
        }
    }

    fun addNewCardToPackage(frontWord: String, backWord: String) {
        viewModelScope.launch {
            try {
                _wordPackageLiveData.value?.let { wordPackage ->
                    iWordRepository.addWordPair(frontWord, backWord, wordPackage.id)
                }
            } catch (ex: Exception) {

            }
        }
    }

    class PackageFactory(
        private val iWordRepository: IWordRepository,
    ) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PackageViewModel(iWordRepository) as T
        }
    }
}