package com.example.myapplication.presentaion.reciever

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.*

const val CHANNEL_ID = 101

class TestNotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == Intent.ACTION_REBOOT || intent?.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            context?.let {
                showNotification(context)
            }
        }
    }

    private fun showNotification(context: Context) {
        val contentIntent = PendingIntent.getBroadcast(
            context, 0,
            Intent(context, TestNotificationBroadcastReceiver::class.java), 0
        )
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID.toString())
                .setSmallIcon(R.drawable.ic_baseline_collections_24)
                .setContentTitle("Повторенье - мать ученья!")
                .setContentText("Повтори слова в нашем приложении, чтобы их не забыть!")
        mBuilder.setContentIntent(contentIntent)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }
}