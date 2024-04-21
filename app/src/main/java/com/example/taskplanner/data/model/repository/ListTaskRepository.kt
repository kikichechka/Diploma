package com.example.taskplanner.data.model.repository

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.taskplanner.App
import com.example.taskplanner.data.NotesDao
import com.example.taskplanner.data.converters.ConverterPendingIntentRequestCode
import com.example.taskplanner.data.model.AlarmClockManager
import com.example.taskplanner.data.model.Day
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

class ListTaskRepository @Inject constructor(private val notesDao: NotesDao) {
    private val calendar = Calendar.getInstance()

    suspend fun getPageDays(firstCount: Int): List<Day> {
        var changeableCount = firstCount
        return List(50) {
            val date = LocalDate.now().minusDays(25).plusDays(changeableCount++.toLong())
            val list = notesDao.getAllNotesByDate(date)
            Day(date, list)
        }
    }

    suspend fun deleteTask(task: TypeTask, context: Context) {
        when (task) {
            is Medications -> {
                notesDao.deleteMedications(task)

                val intent = AlarmClockManager.createIntent(
                    ConverterPendingIntentRequestCode.convertMedicationsToRequestCode(task),
                    task,
                    context
                )
                (context.applicationContext as App).alarmManager.cancel(intent)
            }

            is Note -> notesDao.deleteNote(task)
            is ProductsWithList -> {
                notesDao.deleteProducts(task.productsName)
                if (task.listProducts != null) {
                    task.listProducts.forEach {
                        notesDao.deleteProduct(it)
                    }
                }
            }

            is Reminder -> {
                notesDao.deleteReminder(task)

                val intent = AlarmClockManager.createIntent(
                    ConverterPendingIntentRequestCode.convertReminderToRequestCode(task),
                    task,
                    context
                )
                (context.applicationContext as App).alarmManager.cancel(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun changeFinishTask(task: TypeTask, context: Context) {

        when (task) {
            is Medications -> {
                task.finished = !task.finished
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

                if (!task.finished && calendar.timeInMillis > Calendar.getInstance().timeInMillis) { // был зачеркнут -> создаем
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

            is Note -> {
                task.finished = !task.finished
                notesDao.updateNote(task)
            }

            is ProductsWithList -> {
                task.productsName.finishedProducts = !task.productsName.finishedProducts
                notesDao.updateProducts(task.productsName)
                if (task.listProducts != null) {
                    task.listProducts.forEach {
                        it.finished = task.productsName.finishedProducts
                        notesDao.updateProduct(it)
                    }
                }
            }

            is Reminder -> {
                task.finished = !task.finished
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
                if (!task.finished && calendar.timeInMillis > Calendar.getInstance().timeInMillis) { // был зачеркнут -> создаем
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
    }

    suspend fun changeFinishProduct(product: Product) {
        val newProduct = product.copy(finished = !product.finished)
        notesDao.updateProduct(newProduct)

        val productList = notesDao.getListProduct(newProduct.productsId)
        checkFinishedProducts(productList, newProduct)
        checkNotFinishedProducts(productList, newProduct)
    }

    private suspend fun checkFinishedProducts(productList: List<Product>, newProduct: Product) {
        val sizeFinishedProductList = productList.filter { p -> !p.finished }.size

        if (sizeFinishedProductList == 0 && newProduct.finished) {
            val changeProducts = notesDao.getProductsById(newProduct.productsId)
            if (changeProducts.finishedProducts != newProduct.finished) {
                changeProducts.finishedProducts = newProduct.finished
                notesDao.updateProducts(changeProducts)
            }
        }
    }

    private suspend fun checkNotFinishedProducts(productList: List<Product>, newProduct: Product) {
        val sizeFinishedProductList = productList.filter { p -> !p.finished }.size

        if (sizeFinishedProductList == 1 && !newProduct.finished) {
            val changeProducts = notesDao.getProductsById(newProduct.productsId)
            if (changeProducts.finishedProducts != newProduct.finished) {
                changeProducts.finishedProducts = newProduct.finished
                notesDao.updateProducts(changeProducts)
            }
        }
    }
}
