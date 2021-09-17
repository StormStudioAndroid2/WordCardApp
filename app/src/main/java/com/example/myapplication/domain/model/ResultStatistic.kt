package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  @property rightAnswers - количество правильных ответов в тесте карточек пакета
 *  @property wrongAnswers - количество неправильных ответов в тесте карточек пакета
 *  @property noAnswer - количество неотвеченных карточек в тесте карточек пакета
 */
@Parcelize
open class ResultStatistic(
    var rightAnswers: Int = 0,
    var wrongAnswers: Int = 0,
    var noAnswer: Int = 0
) : Parcelable {
    fun count(): Int = rightAnswers + wrongAnswers + noAnswer
}