package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.model.AnswerType
import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.domain.model.WordPackage

/**
 * ViewModel для фрагмента, показывающего статистику пользователя
 * @property rightAnswerPercent - LiveData, показывающая процент правильных ответов
 * @property wrongAnswerPercent - LiveData, показывающая процент неправильных ответов
 * @property notAnswerPercent - LiveData, показывающая процент неотвеченных карточек(планируется добавить в будущем)
 */
class WordPairCardStatisticViewModel : ViewModel() {

    private var _rightAnswerPercent = MutableLiveData(0f)
    val rightAnswerPercent: LiveData<Float>
        get() = _rightAnswerPercent
    private var _wrongAnswerPercent = MutableLiveData(0f)
    val wrongAnswerPercent: LiveData<Float>
        get() = _wrongAnswerPercent
    private var _notAnswerPercent = MutableLiveData(0f)
    val notAnswerPercent: LiveData<Float>
        get() = _notAnswerPercent

    /**
     *  размещает результаты правильных и неправильных ответов в LiveData
     * @param resultStatistic - Результаты тестирования пользования
     */
    fun onResultStatisticReceived(resultStatistic: ResultStatistic) {
        _rightAnswerPercent.postValue(
            resultStatistic.rightAnswers
                .toFloat() / resultStatistic.count().toFloat()
        )
        _wrongAnswerPercent.postValue(
            resultStatistic.wrongAnswers
                .toFloat() / resultStatistic.count().toFloat()
        )
        _notAnswerPercent.postValue(
            resultStatistic.noAnswer
                .toFloat() / resultStatistic.count().toFloat()
        )
    }
}