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

/**
 *  Репозиторий для доступа к базе данных
 *  @param wordRoomDatabase - база данных
 */
class WordRoomRepository(private val wordRoomDatabase: WordRoomDatabase) : IWordRepository {

    /**
     *  Возвращает Flow со списком всех пакетов без карточек
     */
    override fun getAllPackages(): Flow<List<WordPackage>> {
        return wordRoomDatabase.wordPackageDao().getAll().map { list ->
            list.map { wordPackageEntity -> wordPackageEntity.convertToDomain() }
        }
    }

    /**
     *  Добавляет пакет в базу данных
     *  @param wordPackage - пакет, который нужно добавить
     */
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

    /**
     *  Возвращает список всех пакетов с карточками
     */
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

    /**
     *  добавляет карточку в пакет
     *  @param frontWord - переднее слово карточки
     *  @param backWord - заднее слово карточки
     *  @param ownerId - id пакета, хранящего карточку
     */
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