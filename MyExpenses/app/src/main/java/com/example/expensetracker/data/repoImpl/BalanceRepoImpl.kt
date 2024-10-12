package com.example.expensetracker.data.repoImpl

import com.example.expensetracker.data.dao.BalanceDao
import com.example.expensetracker.data.model.Balance
import com.example.expensetracker.data.repo.BalanceRepo
import javax.inject.Inject

class BalanceRepoImpl @Inject constructor(
    private val dao : BalanceDao
) : BalanceRepo
{
    override suspend fun upsertBalance(balance: Balance) = dao.upsertBalance(balance)
    override suspend fun getBalance(): Balance? = dao.getBalance()
}