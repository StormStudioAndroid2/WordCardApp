package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  класс, хранящий инфу об одной карточке
 *  @property frontWord - слово с передней стороны
 *  @property backWord - слово с задней стороны
 *  @property wordPackageOwnerId - id пакета, в котором хранится эта карточка
 *  @property wordPairId - id самой карточки
 *  @property answer - Отвечена ли карточка(планировалось добавить позднее, не успел)
 */
@Parcelize
data class WordPair(
    var frontWord: String,
    var backWord: String,
    var wordPackageOwnerId: Long,
    var wordPairId: Long,
    var answer: AnswerType
) : Parcelable