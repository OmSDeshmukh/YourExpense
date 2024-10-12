package com.example.expensetracker.data.repo

import com.example.expensetracker.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepo
{
    suspend fun upsertCategory(category: Category)
    fun getAllCategories() : Flow<List<Category>>
    suspend fun getCategoryById(id: Int) : Category
    suspend fun deleteCategoryById(id: Int)
}