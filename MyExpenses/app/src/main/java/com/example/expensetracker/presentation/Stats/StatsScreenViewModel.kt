package com.example.expensetracker.presentation.Stats

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.repo.CategoryRepo
import com.example.expensetracker.data.repo.ItemRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class StatsScreenViewModel @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val itemRepo: ItemRepo,
) : ViewModel()
{
    private val _state = MutableStateFlow(StatsScreenState())
    val state : StateFlow<StatsScreenState> = _state

    init {
        viewModelScope.launch {
            val month = String.format("%02d", LocalDate.now().monthValue)
            val year = LocalDate.now().year.toString()

            itemRepo.getItemsByMonth(month, year).collect { items ->
                _state.value = _state.value.copy(
                    date = LocalDate.now(),
                    items = items
                )
            }
        }

        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            categoryRepo.getAllCategories().collect { categories ->
                _state.value = _state.value.copy(
                    categories = categories
                )
            }
        }
    }

    private fun fetchItems(date : LocalDate) {
        viewModelScope.launch {
            itemRepo.getItemsByDate(date.format(DateTimeFormatter.ISO_LOCAL_DATE)).collect { items ->
                _state.value = _state.value.copy(
                    date = date,
                    items = items
                )
            }
        }
    }

    fun onEvent(event : StatsScreenEvent) {
        when(event) {
            is StatsScreenEvent.OnDateChanged -> {
                when (event.which)
                {
                    "Month" -> {
                        viewModelScope.launch {
                            val month = String.format("%02d", event.date.monthValue)
                            val year = event.date.year.toString()

                            itemRepo.getItemsByMonth(month, year).collect { items ->
                                _state.value = _state.value.copy(
                                    date = event.date,
                                    items = items
                                )
                            }
                        }
                    }
                    "Year" -> {
                        val year = event.date.year.toString()

                        viewModelScope.launch {
                            itemRepo.getItemsByYear(year).collect { items ->
                                _state.value = _state.value.copy(
                                    date = event.date,
                                    items = items
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}