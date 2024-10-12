package com.example.expensetracker.data.repoImpl

import com.example.expensetracker.data.dao.CategoryDao
import com.example.expensetracker.data.model.Category
import com.example.expensetracker.data.repo.CategoryRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(
    private val dao : CategoryDao
) : CategoryRepo
{
    override suspend fun upsertCategory(category: Category) = dao.upsertCategory(category)
    override fun getAllCategories(): Flow<List<Category>> = dao.getAllCategories()
    override suspend fun getCategoryById(id: Int): Category = dao.getCategoryById(id)
    override suspend fun deleteCategoryById(id: Int) = dao.deleteCategoryById(id)
}