package com.example.expensetracker.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetracker.data.dao.BalanceDao
import com.example.expensetracker.data.dao.CategoryDao
import com.example.expensetracker.data.dao.ItemDao
import com.example.expensetracker.data.dao.PotDao
import com.example.expensetracker.data.model.Balance
import com.example.expensetracker.data.model.Category
import com.example.expensetracker.data.model.Item
import com.example.expensetracker.data.model.Pot

@RequiresApi(Build.VERSION_CODES.O)
@Database(
    entities = [Balance::class, Category::class, Item::class, Pot::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun itemDao() : ItemDao
    abstract fun categoryDao() : CategoryDao
    abstract fun potDao() : PotDao
    abstract fun balanceDao() : BalanceDao
}