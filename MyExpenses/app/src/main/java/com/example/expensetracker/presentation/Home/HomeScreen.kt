package com.example.expensetracker.presentation.Home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.data.model.Category
import com.example.expensetracker.data.model.Item
import com.example.expensetracker.data.model.ItemType
import com.example.expensetracker.presentation.components.DateBar
import com.example.expensetracker.presentation.destinations.CameraScreenRouteDestination
import com.example.expensetracker.presentation.destinations.PotsScreenRouteDestination
import com.example.expensetracker.presentation.destinations.ProfileScreenRouteDestination
import com.example.expensetracker.presentation.destinations.StatsScreenRouteDestination
import com.example.expensetracker.presentation.theme.color1
import com.example.expensetracker.presentation.theme.color2
import com.example.expensetracker.presentation.theme.color3
import com.example.expensetracker.presentation.theme.color4
import com.example.myexpenses.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.time.LocalDate

@RootNavGraph(start = true)
@Destination
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel : HomeScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    HomeScreen(
        viewModel = viewModel,
        state = state,
        onEvent = viewModel::onEvent,
        navigator = navigator,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HomeScreen(
    viewModel: HomeScreenViewModel,
    state : HomeScreenState,
    onEvent : (HomeScreenEvent) -> Unit,
    navigator: DestinationsNavigator,
) {
    val tabs = listOf("Day", "Month", "Year")

    val pagerState = rememberPagerState(pageCount = {tabs.size})
    val coroutineScope = rememberCoroutineScope()

    var selectedItem by remember { mutableStateOf(0) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var onLongPress by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    if( deleteDialog )
    {
        AlertDialog(
            onDismissRequest = { deleteDialog = false },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(HomeScreenEvent.OnDeleteItemClicked)
                        deleteDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { deleteDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Day To Day Expenses") },
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
                                        onLongPress = false
                                        deleteDialog = true
                                    }
                            )

                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .clickable {
                                        onLongPress = false
                                        onEvent(HomeScreenEvent.OnRefresh)
                                    }
                            )
                        }
                    }
                }
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
                                0 -> {}
                                1 -> navigator.navigate(PotsScreenRouteDestination)
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
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color1)
            ) {
                CategoryTabRow(
                    pagerState = pagerState,
                    tabs = tabs,
                    onTabSelected = { index ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                            onEvent(HomeScreenEvent.OnPagerPageChanged(index))
                            onEvent(HomeScreenEvent.OnDateChanged(LocalDate.now()))
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            when (pagerState.currentPage) {
                                0 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        DateBar(
                                            currentPage = pagerState.currentPage,
                                            date = state.date,
                                            onDateChanged = { onEvent(HomeScreenEvent.OnDateChanged(it)) },
                                        )

                                        val credits = state.items.filter { it.type == ItemType.CREDIT }
                                        val debits = state.items.filter { it.type == ItemType.DEBIT }

                                        val selectedItemId = state.itemId

                                        Credits(
                                            items = credits,
                                            categories = state.categories,
                                            selectedItemId = selectedItemId,
                                            onItemLongPress = { id ->
                                                onEvent(HomeScreenEvent.OnItemLongPress(id))
                                                onLongPress = true
                                            }
                                        )
                                        Debits(
                                            items = debits,
                                            categories = state.categories,
                                            selectedItemId = selectedItemId,
                                            onItemLongPress = { id ->
                                                onEvent(HomeScreenEvent.OnItemLongPress(id))
                                                onLongPress = true
                                            },
                                            viewModel = viewModel
                                        )
                                    }
                                }
                                1 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        DateBar(
                                            currentPage = pagerState.currentPage,
                                            date = state.date,
                                            onDateChanged = { onEvent(HomeScreenEvent.OnDateChanged(it)) },
                                        )
                                        StatsBar(state = state)
                                        MonthlyData(state = state)
                                        Spacer(Modifier.height(144.dp))
                                    }
                                }
                                2 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        DateBar(
                                            currentPage = pagerState.currentPage,
                                            date = state.date,
                                            onDateChanged = { onEvent(HomeScreenEvent.OnDateChanged(it)) },
                                        )
                                        YearlyData(state = state)
                                        Spacer(Modifier.height(144.dp))
                                    }
                                }
                            }
                        }
                    }

                    item {
                        var categoriesList by remember { mutableStateOf(false) }

                        if( categoriesList ) {
                            AlertDialog(
                                onDismissRequest = { categoriesList = false },
                                title = { Text("Select a Category") },
                                text = {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(3)
                                    ) {
                                        items(state.categories) { category ->
                                            Card(
                                                modifier = Modifier
                                                    .padding(4.dp)
                                                    .clickable {
                                                        onEvent(HomeScreenEvent.OnItemCategoryChanged(category.id))
                                                        categoriesList = false
                                                    },
                                                shape = RoundedCornerShape(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color(category.color),
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(8.dp),
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Text(category.name)
                                                }
                                            }
                                        }
                                    }
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            onEvent(HomeScreenEvent.OnItemCategoryChanged(null))
                                            categoriesList = false
                                        }
                                    ) { Text("None") }
                                },
                                dismissButton = {}
                            )
                        }

                        if (showBottomSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    onLongPress = false
                                    showBottomSheet = false
                                },
                                sheetState = sheetState
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
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
                                                .width(150.dp)
                                                .clickable { onEvent(HomeScreenEvent.OnItemTypeChanged) },
                                            shape = RoundedCornerShape(topStart = 100f, bottomStart = 100f),
                                            border = BorderStroke(width = 0.5.dp, color = Color.Black),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (state.itemType == ItemType.CREDIT) color4 else Color.Gray,
                                                contentColor = if (state.itemType == ItemType.CREDIT) Color.White else color4
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) { Text("Income (Credit)") }
                                        }
                                        Card(
                                            modifier = Modifier
                                                .height(30.dp)
                                                .width(150.dp)
                                                .clickable { onEvent(HomeScreenEvent.OnItemTypeChanged) },
                                            shape = RoundedCornerShape(topEnd = 100f, bottomEnd = 100f),
                                            border = BorderStroke(width = 1.dp, color = Color.Black),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (state.itemType == ItemType.DEBIT) color4 else Color.Gray,
                                                contentColor = if (state.itemType == ItemType.DEBIT) Color.White else color4
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) { Text("Expense (Debit)") }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if( state.itemCategory == null ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.categories),
                                                contentDescription = "Add",
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clickable { categoriesList = true } )
                                        }
                                        else
                                        {
                                            val category : Category = state.categories.find { it.id == state.itemCategory }!!

                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Color(category.color))
                                            ) {
                                                Text("${category.name[0]}",
                                                    modifier = Modifier.align(Alignment.Center).clickable { categoriesList = true },
                                                    fontSize = 20.sp,
                                                    color = Color.White)
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(4.dp))

                                        TextField(
                                            value = state.itemName,
                                            onValueChange = { onEvent(HomeScreenEvent.OnItemNameChanged(it)) },
                                            placeholder = { Text("Item Name") },
                                            singleLine = true,
                                            modifier = Modifier.weight(2f))

                                        Spacer(modifier = Modifier.width(4.dp))

                                        TextField(
                                            value = state.itemPrice,
                                            onValueChange = { onEvent(HomeScreenEvent.OnItemPriceChanged(it)) },
                                            placeholder = { Text("Item Price") },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.weight(1f))
                                    }

                                    Spacer(modifier = Modifier.height(28.dp))

                                    val color = if( state.itemName.isNotEmpty() && state.itemPrice.isNotEmpty() ) Color.Green else Color.Gray

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
                                                .background(color)
                                                .clickable {
                                                    if (color == Color.Green) {
                                                        onEvent(HomeScreenEvent.OnAddItemClicked)
                                                        showBottomSheet = false
                                                        onLongPress = false
                                                    }
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
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp, end = 16.dp)
            ) {
                FloatingActionButton(
                    onClick = { navigator.navigate(CameraScreenRouteDestination) },
                    containerColor = color4,
                    contentColor = Color.White
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp))
                }

                FloatingActionButton(onClick = { showBottomSheet = true },
                    containerColor = color4,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Edit")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryTabRow(
    pagerState: PagerState,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = color4,
        contentColor = Color.White,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .padding(horizontal = 20.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = Color.White)
            )
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = { onTabSelected(index) },
                text = { Text(tab) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Credits(
    items : List<Item>,
    categories : List<Category>,
    selectedItemId : Int?,
    onItemLongPress : (Int?) -> Unit
) {
    val total = items.sumOf { it.amount }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = color2,
                contentColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text( text = "Total Income (Credit)" )
                Text( text = "Rs. ${String.format("%.02f", total)}" )
            }
        }

        items.forEach {item ->
            ItemView(
                item = item,
                categories = categories,
                isSelected = (item.id == selectedItemId),
                onItemLongPress = { onItemLongPress(item.id) }
            )
        }

        if( items.isEmpty() )
            Text("Top on '+' to add new item and long press on entry to edit",
                modifier = Modifier.padding(4.dp),
                color = color4)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Debits(
    items: List<Item>,
    categories : List<Category>,
    selectedItemId: Int?,
    onItemLongPress: (Int?) -> Unit,
    viewModel: HomeScreenViewModel
) {
    val total = items.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = color2,
                contentColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total Expense (Debit)")
                Text(text = "Rs. ${String.format("%.02f", total)}")
            }
        }

        items.forEach { item ->
            ItemView(
                item = item,
                categories = categories,
                isSelected = (item.id == selectedItemId),
                onItemLongPress = { itemId -> onItemLongPress(itemId) }
            )
        }

        if (items.isEmpty())
            Text("Tap on '+' to add a new item and long press on an entry to edit",
                modifier = Modifier.padding(4.dp),
                color = color4)

//        when (val response = viewModel.data.value)
//        {
//            is NetworkResponse.Success -> {
//                val data = response.data
//                Text("Data: ${data}")
//            }
//            is NetworkResponse.Error -> {
//                val message = response.message
//                Text("Error: $message")
//            }
//            is NetworkResponse.Loading -> {
//                Text("Loading...")
//            }
//        }
    }
}

@Composable
fun ItemView(
    item: Item,
    categories : List<Category>,
    isSelected: Boolean,
    onItemLongPress: (Int?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(if (isSelected) Color.Gray else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val category = categories.find { it.id == item.categoryId }

            if( category != null )
            {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(category.color))
                ) {
                    Text("${category.name[0]}",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 18.sp,
                        color = Color.White)
                }
            }

            Text(text = item.name, color = color4,
                modifier = Modifier.clickable { onItemLongPress(item.id) }.padding(2.dp) )
        }

        Text(text = "Rs. ${String.format("%.2f", item.amount)}", color = color4,
            modifier = Modifier.clickable { onItemLongPress(item.id) } )
    }
}

@Composable
fun StatsBar(
    state : HomeScreenState
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = color1,
                contentColor = color4
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text( text = "Total Income (Credit)" )
                Text( text = "Rs. ${state.monthlyIncome}" )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = color1,
                contentColor = color4
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text( text = "Total Expense (Debit)" )
                Text( text = "Rs. ${state.monthlyExpense}" )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = color1,
                contentColor = color4
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text( text = "Balance" )
                Text( text = "Rs. ${state.monthlyBalance}" )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyData(
    state: HomeScreenState
) {
    val len = state.date.lengthOfMonth()

    var balance = 0.0

    for( i in 1..len )
    {
        val creditSum: Double
        val debitSum: Double

        val creditItems = state.items.filter { it.date.dayOfMonth == i && it.type == ItemType.CREDIT }
        creditSum = creditItems.sumOf { it.amount }

        val debitItems = state.items.filter { it.date.dayOfMonth == i && it.type == ItemType.DEBIT }
        debitSum = debitItems.sumOf { it.amount }

        if( creditItems.isNotEmpty() || debitItems.isNotEmpty() )
        {
            balance += creditSum - debitSum

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .padding(2.dp),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = color3,
                    contentColor = Color.White
                ),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text( text = "$i  ${state.date.month}  ${state.date.year}" )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text( text = "Income (Credit)" )
                            }

                            creditItems.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text( text = it.name,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 6.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis)

                                    Text( text = "Rs. ${String.format("%.2f", it.amount)}",
                                        color = Color.White,
                                        fontSize = 14.sp)
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text( text = "Expense (Debit)" )
                            }

                            debitItems.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text( text = it.name,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 6.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis)

                                    Text( text = "Rs. ${String.format("%.2f", it.amount)}",
                                        color = Color.White,
                                        fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text("Balance = Rs. $balance")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YearlyData(
    state: HomeScreenState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("\t\t\t")
            Text("Income (Credit)", color = color4)
            Text("Expense (Debit)", color = color4)
            Text("Balance", color = color4)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("C / F", color = color4)
            Text("${state.yearlyBalance}", color = color4)
        }

        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        months.forEach {month ->
            val monthlyItems = state.items.filter { it.date.month.toString().substring(0, 3) == month.uppercase() }
            val monthlyIncome = monthlyItems.filter { it.type == ItemType.CREDIT }.sumOf { it.amount }
            val monthlyExpense = monthlyItems.filter { it.type == ItemType.DEBIT }.sumOf { it.amount }
            val monthlyBalance = monthlyIncome - monthlyExpense

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(month, modifier = Modifier .weight(0.75f), color = color4)

                    Text("$monthlyIncome",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 24.dp),
                        color = color4,
                        textAlign = TextAlign.End)

                    Text("$monthlyExpense",
                        modifier = Modifier
                            .weight(1f),
                        color = color4,
                        textAlign = TextAlign.End)

                    Text("$monthlyBalance",
                        modifier = Modifier
                            .weight(1f),
                        color = color4,
                        textAlign = TextAlign.End)
                }

                HorizontalDivider(color = color4)
            }
        }
    }
}