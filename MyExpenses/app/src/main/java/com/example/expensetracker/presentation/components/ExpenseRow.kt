package com.example.expensetracker.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.model.Item

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseRow(
    color: Color,
    item : Item,
    onItemLongPress : (Int?) -> Unit
) {
    var selected by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .pointerInput(Unit) {
                detectTapGestures (
                    onLongPress = {
                        onItemLongPress(item.id)
                        selected = !selected
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if( item.categoryId != null )
            {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 6.dp)
                        .background(Color.Red)
                ) {
//                    Text("${item.category.name[0]}", modifier = Modifier .align(Alignment.Center))
                }
            }

            Text( text = item.name, color = color )
        }

        Text( text = "Rs. ${String.format("%.2f", item.amount)}", color = color )
    }
}