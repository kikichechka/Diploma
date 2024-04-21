package com.example.taskplanner.data.converters

import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Reminder

object ConverterPendingIntentRequestCode {
    fun convertReminderToRequestCode(reminder: Reminder): Int {
        return reminder.run {
            date.year +
                    date.monthValue +
                    date.dayOfMonth +
                    time.hour +
                    time.minute +
                    title.hashCode()
        }
    }

    fun convertMedicationsToRequestCode(medications: Medications): Int {
        return medications.run {
            date.year +
                    date.monthValue +
                    date.dayOfMonth +
                    title.hashCode() +
                    time.hour +
                    time.minute +
                    title.hashCode()
        }
    }
}
