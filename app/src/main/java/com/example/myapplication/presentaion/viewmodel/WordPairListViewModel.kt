package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class WordPairListViewModel @Inject constructor(
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

}