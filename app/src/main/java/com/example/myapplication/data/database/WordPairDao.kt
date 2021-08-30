package com.example.myapplication.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordPairDao  {
    @Query("SELECT * FROM word_pair")
    fun getAll(): Flow<List<WordPairEntity>>

    @Delete
    suspend fun delete(wordPairEntity: WordPairEntity)

    @Update
    suspend fun update(wordPairEntity: WordPairEntity)

    @Insert
    suspend fun insertAll(vararg wordPairEntity: WordPairEntity)
}