package com.example.expensetracker.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val name : String,
    val color : Int = Color.Transparent.toArgb()
)
