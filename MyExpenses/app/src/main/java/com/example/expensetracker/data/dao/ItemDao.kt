package com.example.expensetracker.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.expensetracker.data.model.Item
import kotlinx.coroutines.flow.Flow
import java.time.Month
import java.time.Year

@Dao
interface ItemDao
{
    @Upsert
    suspend fun upsertItem(item: Item)

    @Query("SELECT * FROM Item")
    fun getAllItems() : Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE date = :date")
    fun getItemsByDate(date: String) : Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE strftime('%m', date) = :month AND strftime('%Y', date) = :year")
    fun getItemsByMonth(month: String, year: String) : Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE strftime('%Y', date) = :year")
    fun getItemsByYear(year: String) : Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE id = :id")
    suspend fun getItemById(id: Int) : Item

    @Query("DELETE FROM Item WHERE id = :id")
    suspend fun deleteItemById(id: Int)
}