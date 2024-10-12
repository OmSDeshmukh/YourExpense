package com.example.expensetracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.presentation.theme.color4
import com.example.myexpenses.R

@Composable
fun StackedFABs() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Padding to avoid FABs too close to the edges
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp), // Space between FABs
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 24.dp) // Align the Column to the bottom-end
        ) {
            // First FAB
            FloatingActionButton(
                onClick = { /* Handle action for FAB 1 */ },
                containerColor = color4,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Add",
                    modifier = Modifier.size(24.dp))
            }

            FloatingActionButton(onClick = {  },
                containerColor = color4,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Edit")
            }
        }
    }
}

@Preview
@Composable
fun PreviewStackedFABs() {
    StackedFABs()
}
