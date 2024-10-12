package com.example.expensetracker.data.repo

import com.example.expensetracker.data.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepo
{
    suspend fun upsertItem(item: Item)
    fun getAllItems() : Flow<List<Item>>
    fun getItemsByDate(date: String) : Flow<List<Item>>
    fun getItemsByMonth(month: String, year: String) : Flow<List<Item>>
    fun getItemsByYear(year: String) : Flow<List<Item>>
    suspend fun getItemById(id: Int) : Item
    suspend fun deleteItemById(id: Int)
}