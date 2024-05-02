package com.example.taskplanner.data.model

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.taskplanner.data.MyReceiver
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import java.time.LocalTime
import java.util.Calendar

object AlarmClockManager {

    fun createIntent(requestCode: Int, task: TypeTask, context: Context): PendingIntent {
        var time = LocalTime.now()
        when (task) {
            is Medications -> time = task.time
            is Reminder -> time = task.time
            else -> {}
        }
        val calendar = Calendar.getInstance()
        calendar.set(
            task.date.year,
            task.date.monthValue - 1,
            task.date.dayOfMonth,
            time.hour,
            time.minute,
            0
        )
        return createAlarmIntent(requestCode, task, context)
    }

    private fun createAlarmIntent(
        requestCode: Int,
        task: TypeTask,
        context: Context
    ): PendingIntent {
        val alarmIntent =
            Intent(context, MyReceiver::class.java).let { intent ->
                intent.putExtra(MyReceiver.STRING_EXTRA_NAME, task.title)
                intent.putExtra(MyReceiver.INT_EXTRA_NAME, requestCode)
                intent.putExtra(MyReceiver.PARCELABLE_EXTRA_NAME, task)
                PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
        return alarmIntent
    }
}
