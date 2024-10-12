package com.example.expensetracker.data.repo

import com.example.expensetracker.data.model.Pot
import kotlinx.coroutines.flow.Flow

interface PotRepo
{
    suspend fun upsertPot(pot: Pot)
    fun getAllPots() : Flow<List<Pot>>
    suspend fun getPotById(id: Int) : Pot
    suspend fun deletePotById(id: Int)
}