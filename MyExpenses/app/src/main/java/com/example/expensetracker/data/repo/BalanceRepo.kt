package com.example.expensetracker.data.repo

import com.example.expensetracker.data.model.Balance

interface BalanceRepo
{
    suspend fun upsertBalance(balance: Balance)
    suspend fun getBalance() : Balance?
}