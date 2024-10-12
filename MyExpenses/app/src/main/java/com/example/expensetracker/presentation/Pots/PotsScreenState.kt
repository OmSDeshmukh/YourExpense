package com.example.expensetracker.presentation.Pots

import com.example.expensetracker.data.model.Pot

data class PotsScreenState(
    val id : Int? = null,
    val potName : String = "",
    val potGoal : Int = 0,
    var potAmount : Int = 0,
    val pots : List<Pot> = emptyList()
)
