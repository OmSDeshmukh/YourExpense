package com.example.expensetracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.expensetracker.data.model.Category
import com.example.expensetracker.data.model.Item
import com.example.expensetracker.data.model.ItemType
import com.example.expensetracker.presentation.NavGraphs
import com.example.expensetracker.presentation.theme.MyExpensesTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }

        setContent {
            MyExpensesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }
}

//val categoriesList = listOf(
//    Category(0, "Snacks"),
//    Category(1, "Transport"),
//    Category(2, "Housing"),
//    Category(3, "Needs"),
//    Category(4, "Wants"),
//    Category(5, ".Dot")
//)

//@RequiresApi(Build.VERSION_CODES.O)
//val items = listOf(
//    Item(0, "Groceries1", 100.0, 0, ItemType.DEBIT, LocalDate.of(2024, 10, 2)), // Snacks
//    Item(1, "Groceries2", 20.0, 0, ItemType.DEBIT, LocalDate.of(2024, 10, 1)),  // Snacks
//    Item(2, "Groceries3", 50.0, 0, ItemType.DEBIT, LocalDate.of(2024, 10, 3)),  // Snacks
//    Item(3, "Groceries4", 60.0, 0, ItemType.CREDIT, LocalDate.of(2024, 10, 2)), // Snacks
//    Item(4, "Gas1", 50.0, 1, ItemType.DEBIT, LocalDate.of(2024, 10, 1)),        // Transport
//    Item(5, "Gas2", 300.0, 1, ItemType.DEBIT, LocalDate.of(2024, 10, 3)),       // Transport
//    Item(6, "Rent", 500.0, 2, ItemType.CREDIT, LocalDate.of(2024, 10, 2)),      // Housing
//    Item(7, "Electricity", 100.0, 3, ItemType.DEBIT, LocalDate.of(2024, 10, 1)),// Needs
//    Item(8, "Water", 50.0, 3, ItemType.DEBIT, LocalDate.of(2024, 10, 3)),       // Needs
//    Item(9, "Food", 500.0, 3, ItemType.DEBIT, LocalDate.of(2024, 10, 2)),       // Needs
//    Item(10, "Something1", 130.0, 4, ItemType.DEBIT, LocalDate.of(2024, 10, 1)),// Wants
//    Item(11, "Something2", 270.0, 5, ItemType.CREDIT, LocalDate.of(2024, 10, 3))// .Dot
//)

/*  TODO
    Ability to crop the image.
    Adding the image from the gallery.
*/
