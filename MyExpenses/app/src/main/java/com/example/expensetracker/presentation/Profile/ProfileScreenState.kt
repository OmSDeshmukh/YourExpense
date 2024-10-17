package com.example.expensetracker.presentation.Profile

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.expensetracker.data.model.Category

data class ProfileScreenState(
    val id: Int? = null,
    val categories : List<Category> = emptyList(),
    val categoryName: String = "",
    val categoryColor: Int = Color.Transparent.toArgb(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance : Double = 0.0
)
