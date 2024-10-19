package com.example.expensetracker.presentation.Pots

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.data.model.Pot
import com.example.expensetracker.presentation.Home.HomeScreenEvent
import com.example.expensetracker.presentation.destinations.HomeScreenRouteDestination
import com.example.expensetracker.presentation.destinations.ProfileScreenRouteDestination
import com.example.expensetracker.presentation.destinations.StatsScreenRouteDestination
import com.example.expensetracker.presentation.theme.color1
import com.example.expensetracker.presentation.theme.color2
import com.example.expensetracker.presentation.theme.color3
import com.example.expensetracker.presentation.theme.color4
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.math.abs

@Destination
@Composable
fun PotsScreenRoute(
    navigator : DestinationsNavigator
) {
    val viewModel : PotsScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    PotsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navigator = navigator
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PotsScreen(
    state : PotsScreenState,
    onEvent : (PotsScreenEvent) -> Unit,
    navigator : DestinationsNavigator
) {
    var selectedItem by remember { mutableStateOf(1) }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val totalSavings = state.pots.sumOf { it.goal - it.sofar }

    if( showBottomSheet )
    {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.potName,
                    onValueChange = { onEvent(PotsScreenEvent.OnPotNameChanged(it)) },
                    label = { Text("Pot Name") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.potGoal.toString().ifEmpty { "" },
                    onValueChange = {
                        val goal = it.toIntOrNull() ?: 0
                        onEvent(PotsScreenEvent.OnPotGoalChanged(goal))
                    },
                    label = { Text("Goal") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .clickable {
                                onEvent(PotsScreenEvent.OnAddPotClicked)
                                showBottomSheet = false
                            }
                    ) {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    var onLongPress by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }
    if (deleteDialog)
    {
        AlertDialog(
            onDismissRequest = { deleteDialog = false },
            title = { Text("Delete Pot") },
            text = { Text("Are you sure you want to delete this pot?") },
            confirmButton = {
                Text("Yes",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            onEvent(PotsScreenEvent.OnDeletePotClicked)
                            deleteDialog = false
                        })
            },
            dismissButton = {
                Text("No",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            deleteDialog = false
                        })
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Pots") },
                colors = TopAppBarDefaults.topAppBarColors(color4),
                actions = {
                    if (onLongPress)
                    {
                        Row {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .clickable {
                                        onLongPress = false
                                        showBottomSheet = true
                                    }
                            )
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .clickable {
                                        deleteDialog = true
                                        onLongPress = false
                                    }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .padding(16.dp),
                containerColor = color3,
                contentColor = Color.White
            ) {
                Icon( Icons.Default.Add, contentDescription = null )
            }
        },
        bottomBar = {
            NavigationBar {
                val items = listOf( "Home", "Pots", "Stats", "Profile" )
                val icons = listOf(
                    Icons.Default.Home,
                    Icons.Default.Person,
                    Icons.Default.Settings,
                    Icons.Default.Person )

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when (index) {
                                0 -> navigator.navigate(HomeScreenRouteDestination)
                                1 -> {}
                                2 -> navigator.navigate(StatsScreenRouteDestination)
                                3 -> navigator.navigate(ProfileScreenRouteDestination)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = color4,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = color1,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = color1 ),
                        modifier = Modifier.clip(RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color1)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Savings Needed",
                    color = color4,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp))

                Text("Rs. $totalSavings /-",
                    color = color4,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp))
            }

            val totalSofar = state.pots.sumOf { it.sofar }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Savings Done",
                    color = color4,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp))

                Text("Rs. $totalSofar /-",
                    color = color4,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp))
            }

            state.pots.forEach {
                SavingsCard(
                    pot = it,
                    state = state,
                    onPotClicked = { onEvent(PotsScreenEvent.OnPotSelected(it.id!!)) },
                    onAmountChanged = { amount -> onEvent(PotsScreenEvent.OnPotAmountChanged(amount)) },
                    onUpdatePot = { i -> onEvent(PotsScreenEvent.OnUpdatePot(i)) },
                    onLongPress = { onLongPress = true }
                )
            }

            if( state.pots.isEmpty() )
                Text("You have no pots yet. Click on the + button to add one.",
                    color = Color.Black,
                    modifier = Modifier.padding(24.dp))
        }
    }
}

@Composable
fun SavingsCard(
    pot : Pot,
    state : PotsScreenState,
    onPotClicked : () -> Unit,
    onAmountChanged : (Int) -> Unit,
    onUpdatePot : (Int) -> Unit,
    onLongPress : () -> Unit
) {
    val progress = (pot.sofar.toFloat() / pot.goal.toFloat()) * 100

    var selected by remember { mutableStateOf(1) }

    var showDialog by remember { mutableStateOf(false) }
    if( showDialog )
    {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(pot.name) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .height(30.dp)
                                .weight(1f)
                                .clickable { selected = 1 },
                            shape = RoundedCornerShape(topStart = 50f, bottomStart = 50f),
                            border = BorderStroke(width = 0.5.dp, color = Color.Black),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected == 1) color4 else Color.Gray,
                                contentColor = if (selected == 1) Color.White else color4
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Add")
                            }
                        }
                        Card(
                            modifier = Modifier
                                .height(30.dp)
                                .weight(1f)
                                .clickable { selected = -1 },
                            shape = RoundedCornerShape(topEnd = 50f, bottomEnd = 50f),
                            border = BorderStroke(width = 0.5.dp, color = Color.Black),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected == -1) color4 else Color.Gray,
                                contentColor = if (selected == -1) Color.White else color4
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Take Out")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value =  state.potAmount.toDouble().toInt().toString().ifEmpty { "" },
                        onValueChange = {
                            val amount = it.toIntOrNull() ?: 0
                            onAmountChanged(amount)
                        },
                        label = { Text("Amount") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Slider(
                        value = state.potAmount.toFloat(),
                        onValueChange = { onAmountChanged(it.toInt()) },
                        valueRange = if( selected == 1 ) 0f..(pot.goal - pot.sofar).toFloat() else 0f..pot.sofar.toFloat(),
                    )
               }
            },
            confirmButton = {
                Text("Save",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            onUpdatePot(selected)
                            showDialog = false
                            selected = 1
                        })
            },
            dismissButton = {
                Text("Cancel",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            showDialog = false
                            selected = 1
                        })
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onPotClicked()
                        onLongPress()
                    },
                    onTap = {
                        if (pot.sofar < pot.goal) {
                            onPotClicked()
                            showDialog = true
                        }
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = color2,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(pot.name, color = Color.White)

                LinearProgressIndicator(
                    progress = { progress / 100 },
                    trackColor = color4,
                    color = color1
                )

                if( pot.sofar >= pot.goal )
                    Text("Congratulations! Savings Achieved!", color = Color.White)
                else
                    Text("Saved Rs. ${pot.sofar}/-   ${String.format("%.2f", progress)} %", color = Color.White)
            }

            Column {
                Text("Goal :", color = Color.White)
                Text("Rs. ${pot.goal}/-", color = Color.White)
            }
        }
    }
}