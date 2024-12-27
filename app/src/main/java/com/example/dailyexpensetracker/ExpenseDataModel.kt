package com.example.dailyexpensetracker

data class ExpenseDataModel(
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: String,
    val description: String?
)
