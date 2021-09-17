package com.example.myapplication.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для взаимодействия с карточками в базе данных
 *
 */
@Dao
interface WordPairDao {
    /**
     *  Возвращает все карточки из базы данных
     */
    @Query("SELECT * FROM word_pair")
    fun getAll(): Flow<List<WordPairEntity>>

    /**
     *  Удаляет указанную карточку из базы данных
     */
    @Delete
    suspend fun delete(wordPairEntity: WordPairEntity)

    /**
     * Обновляет указанную карточку в базе данных
     */
    @Update
    suspend fun update(wordPairEntity: WordPairEntity)

    /**
     * Вставляет все указанные карточки в базу данных
     */
    @Insert
    suspend fun insertAll(vararg wordPairEntity: WordPairEntity)
}