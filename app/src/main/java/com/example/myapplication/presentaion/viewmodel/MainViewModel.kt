package com.example.myapplication.presentaion.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.presentaion.utils.ViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject

class MainViewModel @Inject constructor(private val roomRepository: IWordRepository) : ViewModel() {
    private val _wordPackageListLiveData = MutableLiveData<List<WordPackage>>()
    val wordPackageListLiveData: LiveData<List<WordPackage>>
        get() = _wordPackageListLiveData
    private val _loadPackageStateLiveData = MutableLiveData<ViewState>()
    val loadPackageStateLiveData: LiveData<ViewState>
        get() = _loadPackageStateLiveData
    private val _insertPackageStateLiveData = MutableLiveData<ViewState>()
    val insertPackageStateLiveData: LiveData<ViewState>
        get() = _insertPackageStateLiveData

    private var allPackages: List<WordPackage> = listOf()

    fun loadWordPackagesFromDatabase() {
        viewModelScope.launch {
            try {
                _loadPackageStateLiveData.postValue(ViewState.LOADING)
                val list = roomRepository.getAllPackages()
                list?.collect { flowList ->
                    allPackages = flowList
                    _wordPackageListLiveData.postValue(flowList)
                    _loadPackageStateLiveData.postValue(ViewState.LOADED)
                } ?: throw NullPointerException("Cannot load from database because null variable!")
            } catch (ex: Exception) {
                _loadPackageStateLiveData.postValue(ViewState.ERROR)
            }
        }
    }

    fun insertWordPackage(wordPackage: WordPackage) {
        viewModelScope.launch {
            try {
                _insertPackageStateLiveData.postValue(ViewState.LOADING)
                val resultInsert = roomRepository.addPackage(wordPackage)
                if (!resultInsert) throw NullPointerException("Cannot insert in database because null variable!")
            } catch (ex: Exception) {
                _insertPackageStateLiveData.postValue(ViewState.ERROR)
            }
        }
    }

    fun onSearchEditTextChanged(searchName: String) {
        _wordPackageListLiveData.postValue(allPackages.filter { p -> p.name.contains(searchName) })
    }
}