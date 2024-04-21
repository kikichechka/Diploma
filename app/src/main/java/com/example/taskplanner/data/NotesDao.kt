package com.example.taskplanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskplanner.data.model.entity.Medications
import com.example.taskplanner.data.model.entity.Note
import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.Products
import com.example.taskplanner.data.model.entity.ProductsWithList
import com.example.taskplanner.data.model.entity.Reminder
import com.example.taskplanner.data.model.entity.TypeTask
import java.time.LocalDate

@Dao
interface NotesDao {
    @Query("SELECT * FROM note WHERE date = :date")
    suspend fun getNotesByDate(date: LocalDate): List<Note>

    @Query("SELECT * FROM reminder WHERE date = :date")
    suspend fun getRemindersByDate(date: LocalDate): List<Reminder>

    @Query("SELECT * FROM products WHERE date_products = :date")
    suspend fun getProductsByDate(date: LocalDate): List<Products>

    @Query("SELECT * FROM products WHERE id_products = :id")
    suspend fun getProductsById(id: Int): Products

    @Query("SELECT * FROM medications WHERE date = :date")
    suspend fun getMedicationsByDate(date: LocalDate): List<Medications>

    suspend fun getProductsWithList(date: LocalDate): List<ProductsWithList> {
        val listProducts = mutableListOf<ProductsWithList>()
        val allProducts = getProductsByDate(date)
        allProducts.forEach {
            listProducts.add(ProductsWithList(it, getListProduct(it.idProducts!!).toMutableList()))
        }
        return listProducts
    }

    suspend fun getAllNotesByDate(date: LocalDate): MutableList<TypeTask> {
        val list = mutableListOf<TypeTask>().apply {
            addAll(
                getNotesByDate(date) +
                        getRemindersByDate(date) +
                        getMedicationsByDate(date) +
                        getProductsWithList(date)
            )
        }
        return list
    }

    @Query("SELECT * FROM products WHERE date_products = :date")
    suspend fun getProducts(date: LocalDate): List<Products>

    @Query("SELECT * FROM product WHERE product_id = :id")
    suspend fun getListProduct(id: Int): List<Product>

    @Insert(entity = Note::class)
    suspend fun insertNote(note: Note)

    @Insert(entity = Reminder::class)
    suspend fun insertReminder(reminder: Reminder)

    @Insert(entity = Products::class)
    suspend fun insertProducts(products: Products): Long

    @Insert(entity = Medications::class)
    suspend fun insertMedications(medications: Medications)

    @Insert(entity = Product::class)
    suspend fun insertProduct(product: Product)

    @Update(entity = Reminder::class)
    suspend fun updateReminder(reminder: Reminder)

    @Update(entity = Products::class)
    suspend fun updateProducts(products: Products)

    @Update(entity = Product::class)
    suspend fun updateProduct(product: Product)

    @Update(entity = Medications::class)
    suspend fun updateMedications(medications: Medications)

    @Update(entity = Note::class)
    suspend fun updateNote(note: Note)

    @Delete(entity = Note::class)
    suspend fun deleteNote(note: Note)

    @Delete(entity = Reminder::class)
    suspend fun deleteReminder(reminder: Reminder)

    @Delete(entity = Products::class)
    suspend fun deleteProducts(products: Products)

    @Delete(entity = Product::class)
    suspend fun deleteProduct(product: Product)

    @Delete(entity = Medications::class)
    suspend fun deleteMedications(medications: Medications)
}
