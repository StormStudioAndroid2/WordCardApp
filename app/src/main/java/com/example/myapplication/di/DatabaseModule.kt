package com.example.myapplication.di

import android.app.Application
import androidx.room.Room
import com.example.myapplication.data.database.WordRoomDatabase
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.data.repository.WordRoomRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideDatabaseModule(application: Application): WordRoomDatabase = Room.databaseBuilder(
        application.applicationContext,
        WordRoomDatabase::class.java,
        "word_database"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideWordRoomRepository(database: WordRoomDatabase  ): IWordRepository = WordRoomRepository(database)
}