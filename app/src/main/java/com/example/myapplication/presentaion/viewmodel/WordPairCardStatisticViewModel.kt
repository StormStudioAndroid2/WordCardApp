package com.example.myapplication.presentaion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.model.AnswerType
import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.domain.model.WordPackage

class WordPairCardStatisticViewModel : ViewModel() {

    var resultStatistic = ResultStatistic()
    private var _rightAnswerPercent = MutableLiveData<Float>(0f)
    val rightAnswerPercent: LiveData<Float>
        get() = _rightAnswerPercent
    private var _wrongAnswerPercent = MutableLiveData<Float>(0f)
    val wrongAnswerPercent: LiveData<Float>
        get() = _wrongAnswerPercent
    private var _notAnswerPercent = MutableLiveData<Float>(0f)
    val notAnswerPercent: LiveData<Float>
        get() = _notAnswerPercent

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