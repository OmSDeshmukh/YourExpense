package com.example.expensetracker.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.expensetracker.data.model.Balance

@Dao
interface BalanceDao
{
    @Upsert
    suspend fun upsertBalance(balance: Balance)

    @Query("SELECT * FROM Balance WHERE id = 1")
    suspend fun getBalance() : Balance?
}