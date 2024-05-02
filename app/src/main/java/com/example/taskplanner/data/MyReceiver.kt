package com.example.taskplanner.data

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.taskplanner.App
import com.example.taskplanner.R
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import com.example.taskplanner.presentation.MainActivity

class MyReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(STRING_EXTRA_NAME)
        val requestCode = intent.getIntExtra(INT_EXTRA_NAME, 0)
        val note = intent.getParcelableExtra(PARCELABLE_EXTRA_NAME, TypeTask::class.java)
        createNotification(context, message ?: "", requestCode, note)
    }

    private fun createNotification(
        context: Context,
        message: String,
        requestCode: Int,
        task: TypeTask?
    ) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val notification = NotificationCompat.Builder(context, App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(getImage(task))
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context)
                .notify(requestCode, notification)
        }
    }

    private fun getImage(task: TypeTask?): Int {
        return when (task) {
            is Medications -> R.drawable.baseline_medication_24
            is Reminder -> R.drawable.baseline_notifications_24
            else -> {
                R.drawable.baseline_notifications_24
            }
        }
    }

    companion object {
        const val STRING_EXTRA_NAME = "title"
        const val INT_EXTRA_NAME = "requestCode"
        const val PARCELABLE_EXTRA_NAME = "typeNote"
    }
}
