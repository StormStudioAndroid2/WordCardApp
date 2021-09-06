package com.example.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.myapplication.data.database.WordRoomDatabase
import com.example.myapplication.presentaion.reciever.CHANNEL_ID


const val CHANNEL_ID = 101
class App : Application() {

    @Volatile
    private lateinit var database: WordRoomDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = WordRoomDatabase.getInstance(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = applicationContext.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID.toString(), name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun getDatabase(): WordRoomDatabase {
        return database
    }

    companion object {
        var instance: App? = null
    }
}