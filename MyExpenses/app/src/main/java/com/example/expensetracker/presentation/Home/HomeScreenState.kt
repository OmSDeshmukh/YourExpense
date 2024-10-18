package com.example.expensetracker.presentation.Home

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.expensetracker.data.model.Category
import com.example.expensetracker.data.model.Item
import com.example.expensetracker.data.model.ItemType
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class HomeScreenState (
    val itemId : Int? = null,
    val itemName : String = "",
    val itemPrice : String = "",
    val itemCategory : Int? = null,
    val itemType: ItemType = ItemType.DEBIT,
    val date : LocalDate = LocalDate.now(),
    val items : List<Item> = emptyList(),
    val categories : List<Category> = emptyList(),

    val monthlyIncome : Double = 0.0,
    val monthlyExpense : Double = 0.0,
    val monthlyBalance : Double = 0.0,

    val yearlyIncome : Double = 0.0,
    val yearlyExpense : Double = 0.0,
    val yearlyBalance : Double = 0.0
)
