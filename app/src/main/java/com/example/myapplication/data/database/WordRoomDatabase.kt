package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *  абстрактный класс базы данных
 */
@Database(entities = arrayOf(WordPackageEntity::class, WordPairEntity::class), version = 1)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordPackageDao(): WordPackageDao

    abstract fun wordPairDao(): WordPairDao
}