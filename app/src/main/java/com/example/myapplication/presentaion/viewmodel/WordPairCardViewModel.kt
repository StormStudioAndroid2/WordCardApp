package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.domain.model.WordPair
import com.yuyakaido.android.cardstackview.Direction
import java.text.FieldPosition

class WordPairCardViewModel() : ViewModel() {

    private val _wordPairListLiveData = MutableLiveData<MutableList<WordPair>>()
    val wordPairListLiveData: LiveData<MutableList<WordPair>>
        get() = _wordPairListLiveData

    private var _isEndOfListLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEndOfListLiveData: LiveData<Boolean>
        get() = _isEndOfListLiveData

    fun setWordPairs(list: List<WordPair>) {
        _wordPairListLiveData.postValue(list.shuffled().toMutableList())
    }

    fun onSwiped(direction: Direction?) {
        if (direction == Direction.Left) {
            onFalseSwiped()
        } else if (direction == Direction.Right) {
            onRightSwiped()
        }
    }

    fun onRightSwiped() {
        onNextPressed()
        wordPairListLiveData.value?.let { list ->
//            list.fir
        }
    }

    fun onFalseSwiped() {
        onNextPressed()
    }

    fun onCardDisappeared(position: Int) {
        if (position != -1) {
            wordPairListLiveData.value?.let {
//                it[position]
                if (it[position] == it.last()) {
                    _isEndOfListLiveData.postValue(true)
                }
            }
        }
    }

    private fun onNextPressed() {

    }
}