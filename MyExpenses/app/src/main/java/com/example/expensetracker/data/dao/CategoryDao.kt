package com.example.expensetracker.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.expensetracker.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao
{
    @Upsert
    suspend fun upsertCategory(category: Category)

    @Query("SELECT * FROM Category")
    fun getAllCategories() : Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE id = :id")
    suspend fun getCategoryById(id: Int) : Category

    @Query("DELETE FROM Category WHERE id = :id")
    suspend fun deleteCategoryById(id: Int)
}