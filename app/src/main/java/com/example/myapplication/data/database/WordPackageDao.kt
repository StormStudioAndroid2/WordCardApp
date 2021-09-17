package com.example.myapplication.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 *  Интерфейс для взаимодействие с пакетами в базе данных
 */
@Dao
interface WordPackageDao {

    /**
     *  Возвращает все пакеты из базы данных
     */
    @Query("SELECT * FROM word_package")
    fun getAll(): Flow<List<WordPackageEntity>>

    /**
     *  Удаляет указанный пакет из базы данных
     */
    @Delete
    suspend fun delete(wordPackageEntity: WordPackageEntity)

    /**
     * Вставляет пакеты в базу данных
     */
    @Insert
    suspend fun insertAll(vararg wordPackageEntity: WordPackageEntity)

    /**
     *  Обновляет указанный пакет в базе данных
     */
    @Update
    fun update(wordPackageEntity: WordPackageEntity)

    /**
     * Возвращает пакеты с карточки, которые к ним прикреплены
     */
    @Transaction
    @Query("SELECT * FROM word_package")
    fun getWordPackageAndPairs(): Flow<List<WordPackageWithWords>>

    /**
     *  Возвращает пакет и его карточки по id пакета
     */
    @Transaction
    @Query("SELECT * FROM word_package WHERE wordPackageId = :wordPackageId ")
    fun getWordPackageAndPairs(wordPackageId: Long): Flow<WordPackageWithWords>
}