package com.example.taskplanner.presentation

import com.example.taskplanner.data.model.entity.Product
import com.example.taskplanner.data.model.entity.TypeNotes

interface ListDaysClickable {
    fun deleteNoteClick(note: TypeNotes)
    fun changeNoteClick(note: TypeNotes)
    fun changeFinishNote(note: TypeNotes)
    fun changeFinishProduct(product: Product)
}