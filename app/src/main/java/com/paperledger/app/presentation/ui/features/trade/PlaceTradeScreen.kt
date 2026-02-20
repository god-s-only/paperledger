package com.paperledger.app.presentation.ui.features.trade

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceTradeScreen(
    navController: NavController,
    viewModel: PlaceTradeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${state.value.symbol} - New Order", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Buy/Sell Toggle
            Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                OrderSideButton(
                    text = "SELL",
                    isSelected = state.value.side == "sell",
                    activeColor = MT5_DOWN,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onEvent(PlaceTradeEvent.OnSideChange(side = "sell"))}
                )
                Spacer(Modifier.width(8.dp))
                OrderSideButton(
                    text = "BUY",
                    isSelected = state.value.side == "buy",
                    activeColor = MT5_UP,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onEvent(PlaceTradeEvent.OnSideChange(side = "buy")) }
                )
            }

            // 2. Order Type TabRow
            TabRow(
                selectedTabIndex = if (state.value.orderType == "market") 0 else 1,
                containerColor = Color.Transparent,
                contentColor = MT5_BLUE,
                divider = {}
            ) {
                Tab(selected = state.value.orderType == "market", onClick = { viewModel.onEvent(PlaceTradeEvent.OnOrderTypeChange(orderType = "market")) }) {
                    Text("Market Execution", modifier = Modifier.padding(12.dp))
                }
                Tab(selected = state.value.orderType == "limit", onClick = { viewModel.onEvent(PlaceTradeEvent.OnOrderTypeChange(orderType = "limit")) }) {
                    Text("Pending Order", modifier = Modifier.padding(12.dp))
                }
            }

            // 3. Inputs Section
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Quantity Field
                OrderInputField(
                    value = state.value.qty,
                    onValueChange = { viewModel.onEvent(PlaceTradeEvent.OnQtyChange(qty = it)) },
                    label = "Quantity (Qty)",
                    placeholder = "0.00"
                )

                // Limit Price (Only for Pending)
                if (state.value.orderType == "limit") {
                    OrderInputField(
                        value = state.value.limitPrice,
                        onValueChange = { viewModel.onEvent(PlaceTradeEvent.OnLimitPriceChange(limitPrice = it)) },
                        label = "Limit Price",
                        placeholder = "Enter target price"
                    )
                }

                // --- STOP LOSS & TAKE PROFIT ROW ---
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        OrderInputField(
                            value = state.value.stopLoss,
                            onValueChange = { viewModel.onEvent(PlaceTradeEvent.OnStopLossChange(it)) },
                            label = "Stop Loss",
                            placeholder = "0.00",
                            labelColor = MT5_DOWN
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        OrderInputField(
                            value = state.value.takeProfit,
                            onValueChange = { viewModel.onEvent(PlaceTradeEvent.OnTakeProfitChange(it)) },
                            label = "Take Profit",
                            placeholder = "0.00",
                            labelColor = MT5_UP
                        )
                    }
                }

                // Time In Force Selector
                TIFSelector(state.value.timeInForce) {
                    viewModel.onEvent(PlaceTradeEvent.OnTimeInForceChange(timeInForce = it))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Execution logic */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.value.side == "buy") MT5_UP else MT5_DOWN
                )
            ) {
                Text(
                    text = if (state.value.orderType == "market") "PLACE MARKET ORDER" else "PLACE PENDING ORDER",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun OrderSideButton(
    text: String,
    isSelected: Boolean,
    activeColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) activeColor else Color.Gray.copy(alpha = 0.1f),
        contentColor = if (isSelected) Color.White else Color.Gray
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun OrderInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    labelColor: Color = Color.Gray
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = labelColor,
            fontWeight = if(labelColor != Color.Gray) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun TIFSelector(selected: String, onSelect: (String) -> Unit) {
    val options = listOf("GTC", "IOC", "FOK")
    Column {
        Text("Time In Force", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                val isSelected = selected.lowercase() == option.lowercase()
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(option.lowercase()) },
                    label = { Text(option) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Default() {
    PlaceTradeScreen(navController = rememberNavController())
}