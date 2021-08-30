package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class WordPairListViewModel(
    private val iWordRepository: IWordRepository
) : ViewModel() {
    private val _WordListPackage: MutableLiveData<WordPackage> =
        MutableLiveData<WordPackage>()

    val wordListPackage: LiveData<WordPackage>
        get() = _WordListPackage


    fun updateList(packageId: Long) {
        viewModelScope.launch {
            try {
                iWordRepository.getPackageWithWordsById(packageId)?.collect { wordPackage ->
                    _WordListPackage.postValue(wordPackage)
                }
            } catch (ex: Exception) {

            }
        }
    }

    fun addNewWordPair(packageId: Long, frontWord: String, backWord: String) {
        viewModelScope.launch {
            try {
                iWordRepository.addWordPair(frontWord, backWord, packageId)
            } catch (ex: Exception) {

            }
        }
    }

    class WordFactory(
        private val iWordRepository: IWordRepository,
    ) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WordPairListViewModel(iWordRepository) as T
        }
    }
}