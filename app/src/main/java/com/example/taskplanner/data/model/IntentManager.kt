package com.example.taskplanner.data.model

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.taskplanner.App
import com.example.taskplanner.data.converters.ConverterPendingIntentRequestCode
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Reminder
import java.util.Calendar

object IntentManager {
    fun cancelMedicationIntent(medications: Medications, context: Context) {
        val intent = AlarmClockManager.createIntent(
            ConverterPendingIntentRequestCode.convertMedicationsToRequestCode(medications),
            medications,
            context
        )
        (context.applicationContext as App).alarmManager.cancel(intent)
    }

    fun cancelReminderIntent(reminder: Reminder, context: Context) {
        val intent = AlarmClockManager.createIntent(
            ConverterPendingIntentRequestCode.convertReminderToRequestCode(reminder),
            reminder,
            context
        )
        (context.applicationContext as App).alarmManager.cancel(intent)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun stopOrResumeMedicationIntent(
        medications: Medications,
        context: Context
    ) {
        val calendar = Calendar.getInstance()
        val intent = AlarmClockManager.createIntent(
            ConverterPendingIntentRequestCode.convertMedicationsToRequestCode(medications),
            medications,
            context
        )
        calendar.set(
            medications.date.year,
            medications.date.monthValue - 1,
            medications.date.dayOfMonth,
            medications.time.hour,
            medications.time.minute,
            0
        )
        if (!medications.finished && calendar.timeInMillis > Calendar.getInstance().timeInMillis) {
            when {
                (context.applicationContext as App).alarmManager.canScheduleExactAlarms() -> {

                    (context.applicationContext as App).alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        intent
                    )
                }
            }
        } else {
            (context.applicationContext as App).alarmManager.cancel(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun stopOrResumeReminderIntent(
        reminder: Reminder,
        context: Context
    ) {
        val calendar = Calendar.getInstance()
        val intent = AlarmClockManager.createIntent(
            ConverterPendingIntentRequestCode.convertReminderToRequestCode(reminder),
            reminder,
            context
        )
        calendar.set(
            reminder.date.year,
            reminder.date.monthValue - 1,
            reminder.date.dayOfMonth,
            reminder.time.hour,
            reminder.time.minute,
            0
        )
        if (!reminder.finished && calendar.timeInMillis > Calendar.getInstance().timeInMillis) {
            when {
                (context.applicationContext as App).alarmManager.canScheduleExactAlarms() -> {
                    (context.applicationContext as App).alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        intent
                    )
                }
            }
        } else {
            (context.applicationContext as App).alarmManager.cancel(intent)
        }
    }
}
