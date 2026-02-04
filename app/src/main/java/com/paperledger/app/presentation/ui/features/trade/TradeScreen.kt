package com.paperledger.app.presentation.ui.features.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("TRADE", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Add, contentDescription = "New Order", tint = MT5_BLUE)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 1. Account Parameters Header (Now White/Transparent)
            AccountMetricsHeader(state.value.balance.toString(), state.value.equity.toString(), state.value.margin.toString(), state.value.margin.toString())

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // 2. Positions Section
                item { SectionHeader("POSITIONS") }
                items(state.value.positions) { position ->
                    TradeItemRow(position)
                    HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.1f))
                }

                // 3. Section Separator
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                }

                // 4. Pending Orders Section
                item { SectionHeader("ORDERS") }
                items(state.value.pendingOrders) { order ->
                    TradeItemRow(order, isPending = true)
                    HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.1f))
                }
            }
        }
    }
}

@Composable
fun AccountMetricsHeader(balance: String, equity: String, margin: String, freeMargin: String) {
    // Removed Card background color to keep it clean/white
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
fun SectionHeader(title: String) {
    // Removed background color for a sleeker look
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.ExtraBold,
        color = MT5_BLUE, // Using MT5 Blue for section titles to pop
        letterSpacing = 1.sp
    )
}

@Composable
fun TradeItemRow(trade: TradeScreenState, isPending: Boolean = false) {
    val profitColor = if (trade.pnl.toString().startsWith("-")) MT5_DOWN else MT5_UP

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(trade.symbol, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = trade.type,
                    color = if (trade.type.contains("Buy")) MT5_BLUE else MT5_DOWN,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(", ${trade.lots}", style = MaterialTheme.typography.labelSmall)
            }
            Text(
                text = "${trade.openPrice} → ${trade.currentPrice}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontFamily = FontFamily.Monospace
            )
        }

        if (!isPending) {
            Text(
                text = trade.profit,
                color = profitColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace
            )
        } else {
            Text(
                text = "PENDING",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TradeOrderRow(trade: OrderItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(trade.symbol, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = trade.type,
                    color = if (trade.type.contains("Buy")) MT5_BLUE else MT5_DOWN,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(", ${trade.quantity}", style = MaterialTheme.typography.labelSmall)
            }
        }
            Text(
                text = "PENDING",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
    }
}

@Preview(showBackground = true)
@Composable
fun TradePreview() {

}