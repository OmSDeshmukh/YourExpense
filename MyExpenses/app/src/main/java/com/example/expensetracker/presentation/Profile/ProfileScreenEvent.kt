package com.example.expensetracker.presentation.Profile

sealed class ProfileScreenEvent
{
    data class CategoryNameChanged(val categoryName : String) : ProfileScreenEvent()
    data class CategoryColorChanged(val categoryColor : Int) : ProfileScreenEvent()
    data class OnCategorySelected(val id : Int) : ProfileScreenEvent()
    data object AddCategory : ProfileScreenEvent()
    data object DeleteCategory : ProfileScreenEvent()
}