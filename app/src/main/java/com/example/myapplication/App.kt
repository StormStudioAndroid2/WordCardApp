package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.database.WordRoomDatabase


class App : Application() {

    @Volatile
    private lateinit var database: WordRoomDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = WordRoomDatabase.getInstance(applicationContext)
    }

    fun getDatabase(): WordRoomDatabase {
        return database
    }

    companion object {
        var instance: App? = null
    }
}