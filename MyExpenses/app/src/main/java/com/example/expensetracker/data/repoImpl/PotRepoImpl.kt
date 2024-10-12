package com.example.expensetracker.data.repoImpl

import com.example.expensetracker.data.dao.PotDao
import com.example.expensetracker.data.model.Pot
import com.example.expensetracker.data.repo.PotRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PotRepoImpl @Inject constructor(
    private val dao : PotDao
) : PotRepo
{
    override suspend fun upsertPot(pot: Pot) = dao.upsertPot(pot)
    override fun getAllPots(): Flow<List<Pot>> = dao.getAllPots()
    override suspend fun getPotById(id: Int): Pot = dao.getPotById(id)
    override suspend fun deletePotById(id: Int) = dao.deletePotById(id)
}