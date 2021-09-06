package com.example.myapplication.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.model.AnswerType
import com.example.myapplication.domain.model.WordPair

@Entity(tableName = "word_pair")
data class WordPairEntity(
    var wordPackageOwnerId: Long,
    @ColumnInfo(name = "word_pair_front_word") var frontWord: String,
    @ColumnInfo(name = "word_pair_back_word") var backWord: String
) {
    @PrimaryKey(autoGenerate = true)
    var wordPairId: Long = 0

    fun convertToDomain(): WordPair = WordPair(
        frontWord = this.frontWord,
        backWord = this.backWord,
        wordPackageOwnerId = this.wordPackageOwnerId,
        wordPairId = this.wordPairId,
        answer = AnswerType.NOT_ANSWER
    )
}
