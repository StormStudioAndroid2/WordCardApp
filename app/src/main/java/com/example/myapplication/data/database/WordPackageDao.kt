package com.example.myapplication.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordPackageDao {

    @Query("SELECT * FROM word_package")
    fun getAll(): Flow<List<WordPackageEntity>>

    @Delete
    suspend fun delete(wordPackageEntity: WordPackageEntity)

    @Insert
    suspend fun insertAll(vararg wordPackageEntity: WordPackageEntity)

    @Update
    fun update(wordPackageEntity: WordPackageEntity)

    @Transaction
    @Query("SELECT * FROM word_package")
    fun getWordPackageAndPairs(): Flow<List<WordPackageWithWords>>

    @Query("DELETE FROM word_package WHERE word_package_name = :name")
    suspend fun deleteByUserId(name: String)

    @Transaction
    @Query("SELECT * FROM word_package WHERE wordPackageId = :wordPackageId ")
    fun getWordPackageAndPairs(wordPackageId: Long): Flow<WordPackageWithWords>
}