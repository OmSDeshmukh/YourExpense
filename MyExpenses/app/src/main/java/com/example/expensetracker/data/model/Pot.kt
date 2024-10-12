package com.example.expensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pot(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    val name : String,
    val goal : Int,
    val sofar : Int
)
