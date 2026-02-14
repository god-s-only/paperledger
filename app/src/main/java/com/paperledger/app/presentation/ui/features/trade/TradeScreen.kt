package com.paperledger.app.presentation.ui.features.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.paperledger.app.core.UIEvent

// MT5 Color Palette
val MT5_BLUE = Color(0xFF2196F3)
val MT5_UP = Color(0xFF4CAF50)
val MT5_DOWN = Color(0xFFF44336)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(
    navController: NavController,
    viewModel: TradeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    var selectedPosition by remember { mutableStateOf<PositionItem?>(null) }
    var selectedOrder by remember { mutableStateOf<OrderItem?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UIEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (state.value.positions.isNotEmpty()) "${state.value.pnl} USD" else "Trade",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (state.value.pnl < 0) MT5_DOWN else MT5_UP
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Add, contentDescription = "New Order", tint = MT5_BLUE)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)) {
                AccountMetricsHeader(
                    state.value.balance.toString(),
                    state.value.equity.toString(),
                    state.value.margin.toString(),
                    state.value.margin.toString()
                )

                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { SectionHeader("POSITIONS") { viewModel.onEvent(TradeScreenEvent.OnCloseAllPositionsClick) } }
                    items(state.value.positions) { position ->
                        TradePositionRow(position) {
                            selectedPosition = position
                            selectedOrder = null
                            showSheet = true
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.1f))
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item { SectionHeader("ORDERS") { viewModel.onEvent(TradeScreenEvent.OnCancelAllPendingOrdersClick) } }
                    items(state.value.pendingOrders) { order ->
                        TradeOrderRow(order) {
                            selectedOrder = order
                            selectedPosition = null
                            showSheet = true
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.1f))
                    }
                }
            }

            // Bottom Sheet Implementation
            if (showSheet) {
                TradeActionBottomSheet(
                    position = selectedPosition,
                    order = selectedOrder,
                    onDismiss = { showSheet = false },
                    sheetState = sheetState,
                    state.value,
                    viewModel
                )
            }
        }
    }
}

@Composable
fun AccountMetricsHeader(balance: String, equity: String, margin: String, freeMargin: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MetricItem("Balance", balance)
            MetricItem("Equity", equity, highlight = true)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MetricItem("Margin", margin)
            MetricItem("Free Margin", freeMargin)
        }
    }
}

@Composable
fun MetricItem(label: String, value: String, highlight: Boolean = false) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = if (highlight) MT5_BLUE else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    onActionClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MT5_BLUE,
            letterSpacing = 1.sp
        )

        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "Options",
                    tint = Color.Gray.copy(alpha = 0.6f)
                )
            }

            // The "Side Modal" (DropdownMenu)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (title.contains("POSITIONS", ignoreCase = true))
                                "Close All Positions"
                            else
                                "Cancel All Pending Orders",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onClick = {
                        expanded = false
                        onActionClick()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MT5_DOWN,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeActionBottomSheet(
    position: PositionItem?,
    order: OrderItem?,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    tradeScreenState: TradeScreenState,
    viewModel: TradeViewModel
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val tabs = listOf("Details", if (position != null) "Close Position" else "Cancel Order")

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = MT5_BLUE,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MT5_BLUE
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Content
            when (selectedTabIndex) {
                0 -> DetailsTabContent(position, order)
                1 -> ActionTabContent(position, order, onDismiss, tradeScreenState, viewModel::onEvent)
            }
        }
    }
}

@Composable
fun DetailsTabContent(position: PositionItem?, order: OrderItem?) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        val symbol = position?.symbol ?: order?.symbol ?: ""
        val type = position?.type ?: order?.type ?: ""
        val price = position?.entryPrice ?: order?.price ?: ""

        DetailRow("Symbol", symbol)
        DetailRow("Type", type)
        DetailRow("Price", price.toString())
        if (position != null) {
            DetailRow("Current Price", position.currentPrice.toString())
            DetailRow("Profit/Loss", "${position.pnl}", color = if (position.pnl >= 0) MT5_UP else MT5_DOWN)
        }
    }
}

@Composable
fun ActionTabContent(
    position: PositionItem?,
    order: OrderItem?,
    onDismiss: () -> Unit,
    tradeScreenState: TradeScreenState,
    onEvent: (TradeScreenEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Descriptive text for the user
        Text(
            text = if (position != null)
                "Confirm market execution to close position"
            else
                "Confirm cancellation of pending order",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        if (order != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${order.symbol} (${order.quantity})",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Large Execution Button
        Button(
            onClick = {
                if (position != null) {
                    onEvent(TradeScreenEvent.OnCloseOpenPositionClick(
                        qty = tradeScreenState.qty,
                        symbolOrAssetId = position.symbol
                    ))
                } else {
                    onEvent(TradeScreenEvent.OnClosePendingOrder(
                        orderId = order?.id ?: ""
                    ))
                }
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (position != null) MT5_DOWN else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (position != null)
                    "CLOSE  ${position.symbol}"
                else
                    "CANCEL ORDER",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, color = color, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun TradePositionRow(trade: PositionItem, onClick: () -> Unit) {
    val profitColor = if (trade.pnl < 0) MT5_DOWN else MT5_UP
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(trade.symbol, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(trade.type, color = if (trade.type.contains("LONG")) MT5_BLUE else MT5_DOWN, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Text(", ${trade.qty}", style = MaterialTheme.typography.labelSmall)
            }
            Text("${trade.entryPrice} → ${trade.currentPrice}", style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontFamily = FontFamily.Monospace)
        }
        Text("${trade.pnl}", color = profitColor, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun TradeOrderRow(trade: OrderItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(trade.symbol, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(trade.type, color = if (trade.side.contains("buy")) MT5_BLUE else MT5_DOWN, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Text(", ${trade.quantity}", style = MaterialTheme.typography.labelSmall)
            }
            Text("${trade.price}", style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontFamily = FontFamily.Monospace)
        }
        Text("PENDING", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun TradePreview() {

}