package com.example.myapplication.data.repository

import android.content.Context
import android.util.Log
import com.example.myapplication.App
import com.example.myapplication.data.database.WordPackageEntity
import com.example.myapplication.data.database.WordPackageWithWords
import com.example.myapplication.data.database.WordPairEntity
import com.example.myapplication.data.database.WordRoomDatabase
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.model.WordPair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WordRoomRepository : IWordRepository {

    override fun getAllPackages(): Flow<List<WordPackage>>? {
        return App.instance?.getDatabase()?.wordPackageDao()?.getAll()?.map { list ->
            list.map { wordPackageEntity -> wordPackageEntity.convertToDomain() }
        }
    }

    override suspend fun addPackage(wordPackage: WordPackage): Boolean {
        val entity = WordPackageEntity(
            wordPackage.name,
            wordPackage.frontLanguage,
            wordPackage.backLanguage
        )
        App.instance?.getDatabase()?.wordPackageDao()?.insertAll(
            entity
        ) ?: return false
        wordPackage.id = entity.wordPackageId
        return true
    }

    override fun getPackageWithWordsById(
        wordPackageId: Long
    ): Flow<WordPackage>? {
        return App.instance?.getDatabase()?.wordPackageDao()?.getWordPackageAndPairs(wordPackageId)
            ?.map { value: WordPackageWithWords ->
                WordPackage(
                    value.wordPackageEntity.wordPackageId,
                    value.wordPackageEntity.name,
                    value.wordPackageEntity.frontLanguage,
                    value.wordPackageEntity.backLanguage,
                    value.wordPairs.map(WordPairEntity::convertToDomain)
                )
            }
    }

    override suspend fun addWordPair(frontWord: String, backWord: String, ownerId: Long) {
        val entity = WordPairEntity(
            ownerId, frontWord, backWord
        )
        App.instance?.getDatabase()?.wordPairDao()?.insertAll(entity)
    }
}