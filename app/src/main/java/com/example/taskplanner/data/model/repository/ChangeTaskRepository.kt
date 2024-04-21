package com.example.taskplanner.data.model.repository

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.taskplanner.App
import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.converters.ConverterPendingIntentRequestCode
import com.example.taskplanner.data.model.AlarmClockManager
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import java.util.Calendar
import javax.inject.Inject

class ChangeTaskRepository @Inject constructor(private val notesDao: NotesDao) {
    private val calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun changeTask(task: TypeTask, context: Context) {
        when(task) {
            is Medications -> {
                notesDao.updateMedications(task)

                val intent = AlarmClockManager.createIntent(
                    ConverterPendingIntentRequestCode.convertMedicationsToRequestCode(task),
                    task,
                    context
                )
                calendar.set(
                    task.date.year,
                    task.date.monthValue - 1,
                    task.date.dayOfMonth,
                    task.time.hour,
                    task.time.minute,
                    0
                )

                if (!task.finished && calendar.timeInMillis > Calendar.getInstance().timeInMillis) {
                    when {
                        (context.applicationContext as App).alarmManager.canScheduleExactAlarms() -> {

                            (context.applicationContext as App).alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                intent
                            )
                        }
                    }
                }
            }
            is Note -> notesDao.updateNote(task)
            is Reminder -> {
                notesDao.updateReminder(task)

                val intent = AlarmClockManager.createIntent(
                    ConverterPendingIntentRequestCode.convertReminderToRequestCode(task),
                    task,
                    context
                )
                calendar.set(
                    task.date.year,
                    task.date.monthValue - 1,
                    task.date.dayOfMonth,
                    task.time.hour,
                    task.time.minute,
                    0
                )

                if (!task.finished && calendar.timeInMillis > Calendar.getInstance().timeInMillis) {
                    when {
                        (context.applicationContext as App).alarmManager.canScheduleExactAlarms() -> {

                            (context.applicationContext as App).alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                intent
                            )
                        }
                    }
                }

            }
            is ProductsWithList -> {
                if (task.listProducts!=null) {
                    task.listProducts.forEach {
                        notesDao.insertProduct(it)
                    }
                }
            }
        }
    }

    suspend fun deleteOldListProduct(listProduct: List<Product>) {
        listProduct.forEach {
            notesDao.deleteProduct(it)
        }
    }
}
