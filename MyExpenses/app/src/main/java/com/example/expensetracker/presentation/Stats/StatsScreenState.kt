package com.example.expensetracker.presentation.Stats

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.expensetracker.data.model.Item
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class StatsScreenState(
    val date : LocalDate = LocalDate.now(),
    val items : List<Item> = emptyList()
)