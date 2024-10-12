package com.example.expensetracker.di

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.expensetracker.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@RequiresApi(Build.VERSION_CODES.O)
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "mycollege.db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun providesCategoryDao(database: AppDatabase) = database.categoryDao()

    @Provides
    @Singleton
    fun providesItemDao(database: AppDatabase) = database.itemDao()

    @Provides
    @Singleton
    fun providesPotDao(database: AppDatabase) = database.potDao()

    @Provides
    @Singleton
    fun providesBalanceDao(database: AppDatabase) = database.balanceDao()
}