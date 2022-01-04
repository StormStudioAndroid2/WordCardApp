package com.example.myapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Collections.swap

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
    var wordPairList: List<WordPair>,
    var isReversed: Boolean = false
) : Parcelable {

    fun reversePackage(): WordPackage {
        return WordPackage(
            id,
            name,
            backLanguage,
            frontLanguage,
            wordPairList.map { WordPair(it.backWord, it.frontWord, it.wordPackageOwnerId, it.wordPairId, it.answer) },
            !isReversed
        )
    }
    companion object {
        fun emptyModel(): WordPackage {
            return  WordPackage(-1, "", "", "", emptyList())
        }
    }
}