package com.example.expensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    val name : String,
    val amount : Double,
    val categoryId: Int?,
    val type : ItemType,
    val date : LocalDate
)

enum class ItemType {
    CREDIT,
    DEBIT
}
