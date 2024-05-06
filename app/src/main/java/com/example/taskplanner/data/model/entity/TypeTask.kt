@file:Suppress("DEPRECATED_ANNOTATION")

package com.example.taskplanner.data.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
sealed class TypeTask(
    open var id: Int?,
    open var date: LocalDate,
    open var title: String,
    open var finished: Boolean
) : Parcelable
