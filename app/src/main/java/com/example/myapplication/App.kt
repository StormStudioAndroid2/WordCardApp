package com.example.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.myapplication.data.database.WordRoomDatabase
import com.example.myapplication.di.*
import com.example.myapplication.presentaion.reciever.CHANNEL_ID
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


const val CHANNEL_ID = 101


@Singleton
@Component(modules = [DatabaseModule::class, SubcomponentsModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance application: Application): ApplicationComponent
    }

    fun mainComponent(): MainComponent.Factory

    fun wordPairComponent(): WordPairComponent.Factory

    fun packageComponent(): PackageComponent.Factory
}

class App : Application() {

    val appComponent = DaggerApplicationComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = applicationContext.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID.toString(), name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}