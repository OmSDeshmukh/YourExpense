package com.example.expensetracker.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.presentation.theme.color1
import com.example.expensetracker.presentation.theme.color2
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateBar(
    currentPage: Int,
    date: LocalDate,
    onDateChanged : (LocalDate) -> Unit,
) {
    val currentDay = LocalDate.now()

    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (isDatePickerDialogOpen)
    {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { isDatePickerDialogOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let { millis ->
                        Instant
                            .ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }

                    selectedDate?.let {
                        onDateChanged(it)
                    }

                    isDatePickerDialogOpen = false
                }) {
                    Text(text = "Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDatePickerDialogOpen = false }) {
                    Text(text = "Cancel")
                }
            },
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = color2,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
                    .clickable {
                        when (currentPage) {
                            0 -> { val dte = date.minusDays(1)
                                onDateChanged(dte)
                            }
                            1 -> { val dte = date.minusMonths(1)
                                onDateChanged(dte)
                            }
                            2 -> { val dte = date.minusYears(1)
                                onDateChanged(dte)
                            }
                        }
                    }
            )

            when( currentPage )
            {
                0 -> {
                    Row(
                        modifier = Modifier.clickable { isDatePickerDialogOpen = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        if( date == currentDay )
                        {
                            Box(
                                modifier = Modifier
                                    .size(30.dp, 30.dp)
                                    .border(2.dp, color1, RoundedCornerShape(4.dp))
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${date.dayOfMonth}", fontSize = 16.sp)
                            }
                        } else {
                            Text("${date.dayOfMonth}", fontSize = 16.sp)
                        }

                        Text("  ${date.month}, ${date.year}  |  ", fontSize = 16.sp,
                            modifier = Modifier.clickable { isDatePickerDialogOpen = true })

                        Text("${date.dayOfWeek}", fontSize = 16.sp,
                            modifier = Modifier.clickable { isDatePickerDialogOpen = true })
                    }
                }

                1 -> { Text("${date.month}, ${date.year}", fontSize = 16.sp,
                    modifier = Modifier.clickable { isDatePickerDialogOpen = true }) }

                2 -> { Text("${date.year}", fontSize = 16.sp,
                    modifier = Modifier.clickable { isDatePickerDialogOpen = true }) }
            }

            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
                    .clickable {
                        when (currentPage) {
                            0 -> { val dte = date.plusDays(1)
                                onDateChanged(dte)
                            }
                            1 -> { val dte = date.plusMonths(1)
                                onDateChanged(dte)
                            }
                            2 -> { val dte = date.plusYears(1)
                                onDateChanged(dte)
                            }
                        }
                    }
            )
        }
    }
}