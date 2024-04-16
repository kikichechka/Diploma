package com.example.taskplanner.data.model.entity

enum class StateType(val value: String) {
    NOTE("заметка"),
    REMINDER("напоминание"),
    PRODUCTS("список покупок"),
    MEDICATIONS("прием лекарств")
}
