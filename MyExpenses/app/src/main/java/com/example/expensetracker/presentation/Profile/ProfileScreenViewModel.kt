package com.example.expensetracker.presentation.Profile

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.model.Category
import com.example.expensetracker.data.model.ItemType
import com.example.expensetracker.data.repo.CategoryRepo
import com.example.expensetracker.data.repo.ItemRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val itemRepo : ItemRepo
) : ViewModel()
{
    private val _state = MutableStateFlow(ProfileScreenState())
    val state : StateFlow<ProfileScreenState> = _state

    init {
        fetchDetails()
        fetchCategories()
    }

    private fun fetchDetails()
    {
        viewModelScope.launch {
            itemRepo.getAllItems().collect { items ->
                val totalIncome = items
                    .filter { it.type == ItemType.CREDIT }
                    .sumOf { it.amount }

                val totalExpense = items
                    .filter { it.type == ItemType.DEBIT }
                    .sumOf { it.amount }

                _state.value = _state.value.copy(
                    totalIncome = totalIncome,
                    totalExpense = totalExpense
                )
            }
        }
    }

    private fun fetchCategories()
    {
        viewModelScope.launch {
            categoryRepo.getAllCategories().collect { categories ->
                _state.value = _state.value.copy(
                    categories = categories
                )
            }
        }
    }

    fun onEvent(event : ProfileScreenEvent)
    {
        when(event)
        {
            is ProfileScreenEvent.CategoryNameChanged -> {
                _state.value = _state.value.copy(
                    categoryName = event.categoryName
                )
            }
            is ProfileScreenEvent.CategoryColorChanged -> {
                _state.value = _state.value.copy(
                    categoryColor = event.categoryColor
                )
            }
            is ProfileScreenEvent.OnCategorySelected -> {
                viewModelScope.launch {
                    val category = categoryRepo.getCategoryById(event.id)
                    _state.value = _state.value.copy(
                        id = category.id,
                        categoryName = category.name,
                        categoryColor = category.color
                    )
                }
            }
            is ProfileScreenEvent.AddCategory -> {
                viewModelScope.launch {
                    categoryRepo.upsertCategory(
                        Category(
                            _state.value.id,
                            _state.value.categoryName,
                            _state.value.categoryColor
                        )
                    )

                    _state.value = _state.value.copy(
                        id = null,
                        categoryName = "",
                        categoryColor = Color.Transparent.toArgb()
                    )
                }
            }
        }
    }
}