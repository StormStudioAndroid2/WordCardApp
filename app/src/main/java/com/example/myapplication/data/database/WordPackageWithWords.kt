package com.example.myapplication.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.myapplication.domain.model.WordPackage

@Entity(primaryKeys = ["wordPackageId", "wordPairId"])
data class WordPackageWithWords(
    @Embedded val wordPackageEntity: WordPackageEntity,
    @Relation(
        parentColumn = "wordPackageId",
        entityColumn = "wordPackageOwnerId"
    )
    val wordPairs: List<WordPairEntity>
) {

    fun convertToDomain(): WordPackage = WordPackage(
        wordPackageEntity.wordPackageId,
        wordPackageEntity.name,
        wordPackageEntity.frontLanguage,
        wordPackageEntity.backLanguage,
        wordPairs.map(WordPairEntity::convertToDomain)
    )
}