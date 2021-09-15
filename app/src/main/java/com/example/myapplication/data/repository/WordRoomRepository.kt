package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.database.WordPackageEntity
import com.example.myapplication.data.database.WordPackageWithWords
import com.example.myapplication.data.database.WordPairEntity
import com.example.myapplication.data.database.WordRoomDatabase
import com.example.myapplication.domain.model.WordPackage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

const val ERROR_ROOM = "ERROR_ROOM"

class WordRoomRepository(private val wordRoomDatabase: WordRoomDatabase) : IWordRepository {


    override fun getAllPackages(): Flow<List<WordPackage>> {
        return wordRoomDatabase.wordPackageDao().getAll().map { list ->
            list.map { wordPackageEntity -> wordPackageEntity.convertToDomain() }
        }
    }

    override suspend fun addPackage(wordPackage: WordPackage): Boolean {
        val entity = WordPackageEntity(
            wordPackage.name,
            wordPackage.frontLanguage,
            wordPackage.backLanguage
        )
        wordRoomDatabase.wordPackageDao().insertAll(
            entity
        )
        wordPackage.id = entity.wordPackageId
        return true
    }

    override fun getPackageWithWordsById(
        wordPackageId: Long
    ): Flow<WordPackage> {
        return wordRoomDatabase.wordPackageDao().getWordPackageAndPairs(wordPackageId)
            .map { value: WordPackageWithWords ->
                WordPackage(
                    value.wordPackageEntity.wordPackageId,
                    value.wordPackageEntity.name,
                    value.wordPackageEntity.frontLanguage,
                    value.wordPackageEntity.backLanguage,
                    value.wordPairs.map(WordPairEntity::convertToDomain)
                )
            }
    }

    override suspend fun addWordPair(frontWord: String, backWord: String, ownerId: Long): Boolean {
        val entity = WordPairEntity(
            ownerId, frontWord, backWord
        )
        return try {
            wordRoomDatabase.wordPairDao().insertAll(entity)
            true
        } catch (ex: Exception) {
            Log.e(ERROR_ROOM, ex.stackTraceToString())
            false
        }
    }
}