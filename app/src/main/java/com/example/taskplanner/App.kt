package com.example.taskplanner

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    lateinit var alarmManager: AlarmManager

    override fun onCreate() {
        super.onCreate()
        alarmManager = this.baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        createNotification()
    }

    private fun createNotification() {
        val name = "Notification channel"
        val descriptionText = "description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = getSystemService((Context.NOTIFICATION_SERVICE)) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "channel_id"
    }
}
