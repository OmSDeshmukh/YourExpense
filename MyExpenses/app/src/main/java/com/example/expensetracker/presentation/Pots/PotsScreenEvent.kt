package com.example.expensetracker.presentation.Pots

sealed class PotsScreenEvent
{
    data class OnPotSelected(val id : Int) : PotsScreenEvent()
    data class OnPotNameChanged(val name : String) : PotsScreenEvent()
    data class OnPotGoalChanged(val goal : Int) : PotsScreenEvent()
    data class OnPotAmountChanged(val amount : Int) : PotsScreenEvent()
    data class OnUpdatePot(val i : Int) : PotsScreenEvent()
    data object OnAddPotClicked : PotsScreenEvent()
    data object OnDeletePotClicked : PotsScreenEvent()
}