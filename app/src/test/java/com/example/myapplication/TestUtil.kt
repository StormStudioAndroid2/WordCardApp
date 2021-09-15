package com.example.myapplication

import com.example.myapplication.data.database.WordPackageEntity
import com.example.myapplication.data.database.WordPairEntity
import com.example.myapplication.domain.model.WordPair

fun createWordPackage(
    title: String = "title",
    firstLanguage: String = "first language",
    secondLanguage: String = "second language"
): WordPackageEntity {
    return WordPackageEntity(title, firstLanguage, secondLanguage)
}

fun createWordPair(
    wordPackageId: Long = 1,
    firstWord: String = "first word",
    secondWord: String = "second word"
): WordPairEntity {
    return WordPairEntity(wordPackageId, firstWord, secondWord)
}
