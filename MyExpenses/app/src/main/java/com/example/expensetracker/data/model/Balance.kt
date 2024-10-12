package com.example.expensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Balance(
    @PrimaryKey
    val id : Int = 1,
    var totalCredit : Double = 0.0,
    var totalDebit : Double = 0.0,
    var balance : Double = 0.0
)
