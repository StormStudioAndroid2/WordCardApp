package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.database.WordPackageWithWords
import com.example.myapplication.domain.model.WordPackage
import kotlinx.coroutines.flow.Flow

interface IWordRepository {
    fun getAllPackages(): Flow<List<WordPackage>>?

    suspend fun addPackage(wordPackage: WordPackage) : Boolean

    suspend fun addWordPair(frontWord: String, backWord: String, ownerId: Long)

    fun getPackageWithWordsById(
        wordPackageId: Long
    ): Flow<WordPackage>?
}