package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  пакет, хранящий карточки
 *  @property id - id в базе данных
 *  @property name - Название пакета
 *  @property frontLanguage - язык, на котором написаны слова с передней стороны карточек
 *  @property backLanguage - язык, на котором написаны слова с задней стороны карточек
 *  @property wordPairList - список всех карточек
 */
@Parcelize
data class WordPackage(
    var id: Long,
    var name: String,
    var frontLanguage: String,
    var backLanguage: String,
    var wordPairList: List<WordPair>
) : Parcelable