package com.example.expensetracker.presentation.Profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.data.model.Category
import com.example.expensetracker.presentation.destinations.HomeScreenRouteDestination
import com.example.expensetracker.presentation.destinations.PotsScreenRouteDestination
import com.example.expensetracker.presentation.destinations.StatsScreenRouteDestination
import com.example.expensetracker.presentation.theme.color1
import com.example.expensetracker.presentation.theme.color2
import com.example.expensetracker.presentation.theme.color4
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProfileScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel : ProfileScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    ProfileScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    state : ProfileScreenState,
    onEvent : (ProfileScreenEvent) -> Unit,
    navigator : DestinationsNavigator
) {
    var selectedItem by remember { mutableStateOf(3) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Day To Day Expenses") },
                colors = TopAppBarDefaults.topAppBarColors(color4),
                actions = { Icon( Icons.Default.MoreVert, contentDescription = null ) }
            )
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
                                1 -> navigator.navigate(PotsScreenRouteDestination)
                                2 -> navigator.navigate(StatsScreenRouteDestination)
                                3 -> {}
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
            LazyColumn {
                item {
                    Box {
                        Column {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                shape = RoundedCornerShape(bottomStart = 300f, bottomEnd = 300f),
                                colors = CardDefaults.cardColors(
                                    containerColor = color2,
                                    contentColor = Color.White
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Welcome Back", fontSize = 18.sp)
                                        Text("Ayushmaan Betapudi", fontSize = 24.sp)
                                    }
                                    Icon(
                                        Icons.Default.Person, contentDescription = null,
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(44.dp))
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.BottomCenter)
                                .height(200.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = color4,
                                contentColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Total Balance : ", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text((state.totalIncome - state.totalExpense).toString(), fontSize = 28.sp)

                                Spacer(modifier = Modifier.height(28.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text("Income", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(state.totalIncome.toString(), fontSize = 20.sp)
                                    }

                                    Column(
                                        modifier = Modifier
                                            .weight(1f),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text("Expenses", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(state.totalExpense.toString(), fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    CategoriesSection (
                        state = state,
                        onCategoryNameChanged = { onEvent(ProfileScreenEvent.CategoryNameChanged(it)) },
                        onCategoryColorChanged = { onEvent(ProfileScreenEvent.CategoryColorChanged(it)) },
                        onAddCategory = { onEvent(ProfileScreenEvent.AddCategory) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriesSection(
    state: ProfileScreenState,
    onCategoryNameChanged: (String) -> Unit,
    onCategoryColorChanged: (Int) -> Unit,
    onAddCategory: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if( showDialog )
    {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Category") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = state.categoryName,
                            onValueChange = { onCategoryNameChanged(it) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = color4,
                                focusedLabelColor = color4,
                                focusedBorderColor = color4,
                                cursorColor = color4
                            )
                        )

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Black)
                                .background(Color(state.categoryColor))
                        )
                    }

                    ColorPicker(
                        color = Color(state.categoryColor),
                        onColorChanged = { onCategoryColorChanged(it) }
                    )
                }
            },
            confirmButton = {
                if( state.categoryName.isNotEmpty() )
                {
                    Text("Add",
                        color = color4,
                        fontSize = 26.sp,
                        modifier = Modifier.clickable {
                            onAddCategory()
                            showDialog = false
                        })
                }
            },
            dismissButton = { Text("Cancel",
                color = color4,
                fontSize = 26.sp,
                modifier = Modifier.clickable { showDialog = false }) },
            containerColor = color1,
            titleContentColor = color4,
            textContentColor = color4,
            iconContentColor = color4,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Categories", color = color4, fontSize = 20.sp)

            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = color4,
                modifier = Modifier.clickable { showDialog = true }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        state.categories.forEach {
            if( it.name.isNotEmpty() )
            {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 6.dp)
                            .background(Color(it.color))
                    ) {
                        Text("${it.name[0]}", modifier = Modifier .align(Alignment.Center))
                    }

                    Text( text = it.name, color = color4 )
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    color: Color,
    onColorChanged : (Int) -> Unit
) {
    val controller = rememberColorPickerController()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color)
        )
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            controller = controller,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                onColorChanged(colorEnvelope.color.toArgb())
            }
        )
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            controller = controller,
        )
    }
}