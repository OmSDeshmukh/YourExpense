package com.example.expensetracker.presentation.Stats

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.presentation.destinations.HomeScreenRouteDestination
import com.example.expensetracker.presentation.destinations.PotsScreenRouteDestination
import com.example.expensetracker.presentation.destinations.ProfileScreenRouteDestination
import com.example.expensetracker.presentation.theme.color1
import com.example.expensetracker.presentation.theme.color4
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.Instant
import java.time.ZoneId
import kotlin.math.round

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun StatsScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel : StatsScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    StatsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navigator = navigator
    )
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatsScreen(
    state : StatsScreenState,
    onEvent : (StatsScreenEvent) -> Unit,
    navigator : DestinationsNavigator
) {
    var selectedItem by remember { mutableStateOf(2) }
    var selectedOption by remember { mutableStateOf("Monthly") }

    Scaffold(
        topBar = {
            var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }

            if (isDatePickerDialogOpen)
            {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = state.date
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
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
                                onEvent(StatsScreenEvent.OnDateChanged(it, "Day"))
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

            val statsList = listOf("Monthly", "Yearly")
            var expanded by remember { mutableStateOf(false) }

            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(selectedOption)

                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Change stats type",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { expanded = !expanded }
                        )

                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    when (selectedOption)
                                    {
                                        "Monthly" -> {
                                            val dte = state.date.minusMonths(1)
                                            onEvent(StatsScreenEvent.OnDateChanged(dte, "Month"))
                                        }
                                        "Yearly" -> {
                                            val dte = state.date.minusYears(1)
                                            onEvent(StatsScreenEvent.OnDateChanged(dte, "Year"))
                                        }
                                    }
                                }
                        )

                        when( selectedOption )
                        {
                            "Monthly" -> { Text("${state.date.month}, ${state.date.year}", fontSize = 16.sp,
                                modifier = Modifier.clickable { isDatePickerDialogOpen = true }) }
                            "Yearly" -> { Text( "${state.date.year}", fontSize = 16.sp,
                                modifier = Modifier.clickable { isDatePickerDialogOpen = true }) }
                        }

                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    when (selectedOption)
                                    {
                                        "Monthly" -> {
                                            val dte = state.date.plusMonths(1)
                                            onEvent(StatsScreenEvent.OnDateChanged(dte, "Month"))
                                        }
                                        "Yearly" -> {
                                            val dte = state.date.plusYears(1)
                                            onEvent(StatsScreenEvent.OnDateChanged(dte, "Year"))
                                        }
                                    }
                                }
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        statsList.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOption = option
                                    when (selectedOption)
                                    {
                                        "Month" -> { onEvent(StatsScreenEvent.OnDateChanged(state.date, "Month")) }
                                        "Yearly" -> { onEvent(StatsScreenEvent.OnDateChanged(state.date, "Year")) }
                                    }
                                    expanded = false
                                },
                                text = { Text(option) }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(color4)
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
                                2 -> {}
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color1)
                .padding(8.dp)
        ) {
            var selected by remember { mutableStateOf(0) }

            LazyColumn {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .height(30.dp)
                                .width(150.dp)
                                .clickable { selected = 0 },
                            shape = RoundedCornerShape(topStart = 100f, bottomStart = 100f),
                            border = BorderStroke(width = 0.5.dp, color = Color.Black),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected == 0) color4 else Color.White,
                                contentColor = if (selected == 0) Color.White else color4
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Income (Credit)")
                            }
                        }
                        Card(
                            modifier = Modifier
                                .height(30.dp)
                                .width(150.dp)
                                .clickable { selected = 1 },
                            shape = RoundedCornerShape(topEnd = 100f, bottomEnd = 100f),
                            border = BorderStroke(width = 1.dp, color = Color.Black),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected == 1) color4 else Color.White,
                                contentColor = if (selected == 1) Color.White else color4
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Expense (Debit)")
                            }
                        }
                    }
                }   // Credit | Debit

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when (selected) {
                                0 -> { Text("Total Income (Credit)", color = color4) }
                                1 -> { Text("Total Expense (Debit)", color = color4) }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("$1234567890", fontSize = 20.sp, color = color4)
                        }

//                        val categories = items.groupBy { it.categoryId?.name ?: "" }.mapValues { entry ->
//                            entry.value.sumOf { it.amount }
//                        }

//                        val totalSpending = categories.values.sum()
                        val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)

                        Canvas(
                            modifier = Modifier
                                .size(200.dp)
                        ) {
                            var startAngle = 0f
                            val radius = size.minDimension / 3
                            val thickness = radius * 0.3f

//                            categories.entries.forEachIndexed { index, categoryEntry ->
//                                val sweepAngle = (categoryEntry.value / totalSpending * 360f).toFloat()
//                                drawArc(
//                                    color = colors[index % colors.size],
//                                    startAngle = startAngle,
//                                    sweepAngle = sweepAngle,
//                                    useCenter = false,
//                                    topLeft = Offset(size.width / 2 - radius, size.height / 2 - radius),
//                                    size = Size(radius * 2, radius * 2),
//                                    style = Stroke(width = thickness)
//                                )
//                                startAngle += sweepAngle
//                            }
                        }
                    }
                }   // Pie Chart

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Text("All Categories", color = color4)

//                    val categories = items.groupBy { it.category?.name ?: "" }.mapValues { entry ->
//                        entry.value.sumOf { it.amount }
//                    }

//                    val totalSpending = categories.values.sum()
                    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.LightGray)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
//                            categories.entries.forEachIndexed { index, categoryEntry ->
//                                Row (
//                                    modifier = Modifier.padding(top = 2.dp),
//                                    verticalAlignment = Alignment.CenterVertically,
//                                ) {
//                                    Box(
//                                        modifier = Modifier
//                                            .size(25.dp)
//                                            .padding(end = 6.dp)
//                                            .background(colors[index])
//                                    ) {
//                                        Text("${categoryEntry.key[0]}", modifier = Modifier.align(Alignment.Center), color = color4)
//                                    }
//
//                                    Text( text = categoryEntry.key, color = color4 )
//                                }
//                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
//                            categories.entries.forEachIndexed { index, categoryEntry ->
//                                Box(
//                                    modifier = Modifier
//                                        .size(25.dp)
//                                        .clip(CircleShape)
//                                        .background(colors[index])
//                                        .padding(top = 2.dp)
//                                )
//                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
//                            categories.entries.forEachIndexed { index, categoryEntry ->
//                                Text(
//                                    text = "${(categoryEntry.value / totalSpending * 360f).toFloat()}",
//                                    modifier = Modifier.padding(top = 2.dp),
//                                    color = color4 )
//                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
//                            categories.entries.forEachIndexed { index, categoryEntry ->
//                                Text(
//                                    text = String.format("%.2f", categoryEntry.value),
//                                    modifier = Modifier.padding(top = 2.dp),
//                                    color = color4 )
//                            }
                        }
                    }
                }   // All Categories

                item {
                    Column {
                        state.items.forEach {
                            Text(text = it.name, color = color4)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(75.dp)) }

                item {
                    if( state.items.isNotEmpty() )
                    {
                        val spendings by remember {
                            derivedStateOf {
                                val graphItems = state.items.sortedBy { it.date }

                                when (selectedOption) {
                                    "Monthly" -> {
                                        graphItems
                                            .groupBy { it.date }
                                            .map { (date, itemsForDay) ->
                                                val totalAmountForDay = itemsForDay.sumOf { it.amount }
                                                date to totalAmountForDay
                                            }
                                            .sortedBy { it.first }
                                    }
                                    "Yearly" -> {
                                        graphItems
                                            .groupBy { it.date.month }
                                            .map { (month, itemsForMonth) ->
                                                val totalAmountForMonth = itemsForMonth.sumOf { it.amount }
                                                month to totalAmountForMonth
                                            }
                                            .sortedBy { it.first }
                                    }
                                    else -> emptyList()
                                }
                            }
                        }

                        val spacing = 150f
                        val graphColor = color4
                        val transparentGraphColor = remember { graphColor.copy(alpha = 0.5f) }

                        val upperValue = spendings.maxOfOrNull { it.second } ?: 0.0
                        val lowerValue = spendings.minOfOrNull { it.second } ?: 0.0

                        val density = LocalDensity.current
                        val textPaint = remember(density) {
                            Paint().apply {
                                color = color4.toArgb()
                                textAlign = Paint.Align.LEFT
                                textSize = density.run { 12.sp.toPx() }
                            }
                        }

                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            val spacePerItem = (canvasWidth - spacing) / spendings.size

                            spendings.indices.forEach { i ->
                                drawContext.canvas.nativeCanvas.apply {
                                    drawText(
                                        if( selectedOption.equals("Monthly") )
                                        {
                                            val date = spendings[i].first.toString()
                                            date.substring(8, 10)
                                        }
                                        else
                                            spendings[i].first.toString(),
                                        spacing + i * spacePerItem,
                                        canvasHeight - 5,
                                        textPaint
                                    )
                                }
                            }

                            val priceStep = (upperValue - lowerValue) / 4f
                            (0..4).forEach { i ->
                                val yPosition = canvasHeight - spacing - i * (canvasHeight - spacing) / 4f
                                val label = lowerValue + priceStep * i
                                drawContext.canvas.nativeCanvas.apply {
                                    drawText(
                                        round(label).toString(),
                                        30f,
                                        yPosition,
                                        textPaint
                                    )
                                }
                            }

                            var lastX = 0f
                            val strokePath = Path().apply {
                                val chartHeight = canvasHeight - spacing
                                for (i in spendings.indices) {
                                    val info = spendings[i]
                                    val nextInfo = spendings.getOrNull(i + 1) ?: spendings.last()

                                    val leftRatio = (info.second - lowerValue) / (upperValue - lowerValue)
                                    val rightRatio = (nextInfo.second - lowerValue) / (upperValue - lowerValue)

                                    val x1 = spacing + i * spacePerItem
                                    val y1 = chartHeight - (leftRatio * chartHeight).toFloat()
                                    val x2 = spacing + (i + 1) * spacePerItem
                                    val y2 = chartHeight - (rightRatio * chartHeight).toFloat()

                                    if (i == 0) moveTo(x1, y1)

                                    lastX = (x1 + x2) / 2f
                                    quadraticBezierTo(x1, y1, lastX, (y1 + y2) / 2f)
                                }
                            }

                            val fillPath = android.graphics.Path(strokePath.asAndroidPath())
                                .asComposePath()
                                .apply {
                                    lineTo(lastX, size.height - spacing)
                                    lineTo(spacing, size.height - spacing)
                                    close()
                                }

                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        transparentGraphColor,
                                        Color.Transparent
                                    ),
                                    endY = size.height - spacing
                                )
                            )

                            drawPath(
                                path = strokePath,
                                color = graphColor,
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )
                        }
                    }
                }   // Graph
            }
        }
    }
}

//                        val spendings = remember { mutableStateOf<List<Pair<Any, Double>>>(emptyList()) }
//                        val graphItems by remember { derivedStateOf { state.items.sortedBy { it.date } } }
//
//                        when(selectedOption)
//                        {
//                            "Monthly" -> {
//                                spendings.value = graphItems
//                                            .groupBy { it.date }
//                                            .map { (date, itemsForDay) ->
//                                                val totalAmountForDay = itemsForDay.sumOf { it.amount }
//                                                date to totalAmountForDay
//                                            }
//                                            .sortedBy { it.first }
//                            }
//
//                            "Yearly" -> {
//                                spendings.value = graphItems
//                                            .groupBy { it.date.month }
//                                            .map { (date, itemsForMonth) ->
//                                                val totalAmountForMonth = itemsForMonth.sumOf { it.amount }
//                                                date to totalAmountForMonth
//                                            }
//                                            .sortedBy { it.first }
//                            }
//                        }

//                        val graphItems by remember { derivedStateOf { state.items.sortedBy { it.date } } }
//
//                        val spendings by remember { derivedStateOf {
//                            graphItems
//                                .groupBy { it.date }
//                                .map { (date, itemsForDay) ->
//                                    val totalAmountForDay = itemsForDay.sumOf { it.amount }
//                                    date to totalAmountForDay
//                                }
//                                .sortedBy { it.first }
//                        } }