package com.example.expensetracker.presentation.Stats

import java.time.LocalDate

sealed class StatsScreenEvent
{
    data class OnDateChanged(val date : LocalDate, val which : String) : StatsScreenEvent()
    data object OnTypeChanged : StatsScreenEvent()
}