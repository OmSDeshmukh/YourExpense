package com.example.expensetracker.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.expensetracker.data.model.Pot
import kotlinx.coroutines.flow.Flow

@Dao
interface PotDao
{
    @Upsert
    suspend fun upsertPot(pot: Pot)

    @Query("SELECT * FROM Pot")
    fun getAllPots() : Flow<List<Pot>>

    @Query("SELECT * FROM Pot WHERE id = :id")
    suspend fun getPotById(id: Int) : Pot

    @Query("DELETE FROM Pot WHERE id = :id")
    suspend fun deletePotById(id: Int)
}