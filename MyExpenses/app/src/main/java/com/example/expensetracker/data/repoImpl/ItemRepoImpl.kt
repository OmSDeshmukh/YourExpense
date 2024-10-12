package com.example.expensetracker.data.repoImpl

import android.util.Log
import com.example.expensetracker.data.dao.ItemDao
import com.example.expensetracker.data.model.Item
import com.example.expensetracker.data.repo.ItemRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepoImpl @Inject constructor(
    private val dao : ItemDao
) : ItemRepo
{
    override suspend fun upsertItem(item: Item) = dao.upsertItem(item)
    override fun getAllItems(): Flow<List<Item>> = dao.getAllItems()
    override fun getItemsByDate(date: String) : Flow<List<Item>> = dao.getItemsByDate(date)
    override fun getItemsByMonth(month: String, year: String) : Flow<List<Item>> = dao.getItemsByMonth(month, year)
    override fun getItemsByYear(year: String) : Flow<List<Item>> = dao.getItemsByYear(year)
    override suspend fun getItemById(id: Int): Item = dao.getItemById(id)
    override suspend fun deleteItemById(id: Int) = dao.deleteItemById(id)
}