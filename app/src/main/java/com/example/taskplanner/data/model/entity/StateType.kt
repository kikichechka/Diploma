package com.example.taskplanner.data.model.entity

enum class StateType(val value: String) {
    NOTE("заметка"),
    REMINDER("напоминание"),
    PRODUCTS("список продуктов"),
    MEDICATIONS("прием лекарств")
}