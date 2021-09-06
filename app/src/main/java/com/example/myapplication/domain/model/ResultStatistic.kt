package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ResultStatistic(
    var rightAnswers: Int = 0,
    var wrongAnswers: Int = 0,
    var noAnswer: Int = 0
) : Parcelable {
    fun count(): Int = rightAnswers + wrongAnswers + noAnswer
}