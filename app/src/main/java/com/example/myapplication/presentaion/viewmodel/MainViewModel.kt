package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.utils.ViewState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val roomRepository: IWordRepository) : ViewModel() {
    private val _wordPackageListLiveData = MutableLiveData<List<WordPackage>>()
    private val _insertPackageStateLiveData = MutableLiveData<ViewState>()
    val insertPackageStateLiveData: LiveData<ViewState>
        get() = _insertPackageStateLiveData

    private var allPackages: List<WordPackage> = listOf()

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