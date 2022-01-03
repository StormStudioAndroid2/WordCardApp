package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.model.WordPair
import com.yuyakaido.android.cardstackview.Direction
import javax.inject.Inject

/**
 *  ViewModel для тестирования набора
 *  @property wordPairListLiveData - LiveData, хранящая список карточек, которые нужно протестировать
 *  @property resultStatistic - объект, хранящий информацию о тесте и о его результатах
 */
class WordPairCardViewModel @Inject constructor(wordPackage: WordPackage) : ViewModel() {

    private val _wordPairListLiveData = MutableLiveData<MutableList<WordPair>>()
    val wordPairListLiveData: LiveData<MutableList<WordPair>>
        get() = _wordPairListLiveData
    val resultStatistic = ResultStatistic()
    private var isLastCard = false
    private var _isEndOfListLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEndOfListLiveData: LiveData<Boolean>
        get() = _isEndOfListLiveData

    /**
     *  Устанавливает список слов в LiveData
     *  @param list - список слов
     */

    init {
        setWordPairs(wordPackage.wordPairList)
    }

    fun setWordPairs(list: List<WordPair>) {
        _wordPairListLiveData.postValue(list.shuffled().toMutableList())
    }

    /**
     *  Обработка свайпа карточки и определение, угадал слово человек или нет
     *  @param direction - показывает, в какую сторону свайпнули карточку
     */
    fun onSwiped(direction: Direction?) {
        if (direction == Direction.Left) {
            onFalseSwiped()
        } else if (direction == Direction.Right) {
            onRightSwiped()
        }
        _isEndOfListLiveData.postValue(isLastCard)
    }

    /**
     *  Действие, когда текущая карточка исчезает с экрана
     *  @param position - показывает позицию текущей карточки
     */
    fun onCardDisappeared(position: Int) {
        wordPairListLiveData.value?.let {
            if (it[position] == it.last()) {
                isLastCard = true
            }
        }
    }

    /**
     * обработка если слово угадано
     */
    private fun onRightSwiped() {
        resultStatistic.rightAnswers++
    }

    /**
     * обработка если слово не угадано
     */
    private fun onFalseSwiped() {
        resultStatistic.wrongAnswers++
    }
}