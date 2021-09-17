package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.WordPackage
import kotlinx.coroutines.flow.Flow

/**
 *  Интерфейс для репозитория
 *  @see WordRoomRepository
 */
interface IWordRepository {
    fun getAllPackages(): Flow<List<WordPackage>>?

    suspend fun addPackage(wordPackage: WordPackage) : Boolean

    suspend fun addWordPair(frontWord: String, backWord: String, ownerId: Long): Boolean

    fun getPackageWithWordsById(
        wordPackageId: Long
    ): Flow<WordPackage>?
}