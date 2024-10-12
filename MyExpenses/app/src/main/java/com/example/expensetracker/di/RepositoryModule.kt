package com.example.expensetracker.di

import com.example.expensetracker.data.repo.BalanceRepo
import com.example.expensetracker.data.repo.CategoryRepo
import com.example.expensetracker.data.repo.ItemRepo
import com.example.expensetracker.data.repo.PotRepo
import com.example.expensetracker.data.repoImpl.BalanceRepoImpl
import com.example.expensetracker.data.repoImpl.CategoryRepoImpl
import com.example.expensetracker.data.repoImpl.ItemRepoImpl
import com.example.expensetracker.data.repoImpl.PotRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule
{
    @Binds
    @Singleton
    abstract fun bindBalanceRepository(
        impl: BalanceRepoImpl
    ): BalanceRepo

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepoImpl
    ): CategoryRepo

    @Binds
    @Singleton
    abstract fun bindItemRepository(
        impl: ItemRepoImpl
    ): ItemRepo

    @Binds
    @Singleton
    abstract fun bindPotRepository(
        impl: PotRepoImpl
    ): PotRepo
}