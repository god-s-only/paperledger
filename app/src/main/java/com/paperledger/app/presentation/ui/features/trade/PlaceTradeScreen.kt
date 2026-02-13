package com.paperledger.app.presentation.ui.features.trade

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceTradeScreen(
    symbol: String = "BTCUSDT",
    navController: NavController
) {
    // Parameter States
    var orderType by remember { mutableStateOf("market") } // market or limit
    var side by remember { mutableStateOf("buy") }         // buy or sell
    var qty by remember { mutableStateOf("") }
    var limitPrice by remember { mutableStateOf("") }
    var timeInForce by remember { mutableStateOf("gtc") } // gtc, ioc, fok

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$symbol - New Order", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            // 1. Buy/Sell Toggle (MT5 Style Segmented Control)
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)) {
                OrderSideButton(
                    text = "SELL",
                    isSelected = side == "sell",
                    activeColor = MT5_DOWN,
                    modifier = Modifier.weight(1f),
                    onClick = { side = "sell" }
                )
                Spacer(Modifier.width(8.dp))
                OrderSideButton(
                    text = "BUY",
                    isSelected = side == "buy",
                    activeColor = MT5_UP,
                    modifier = Modifier.weight(1f),
                    onClick = { side = "buy" }
                )
            }

            // 2. Order Type TabRow
            TabRow(
                selectedTabIndex = if (orderType == "market") 0 else 1,
                containerColor = Color.Transparent,
                contentColor = MT5_BLUE,
                divider = {}
            ) {
                Tab(selected = orderType == "market", onClick = { orderType = "market" }) {
                    Text("Market Execution", modifier = Modifier.padding(12.dp))
                }
                Tab(selected = orderType == "limit", onClick = { orderType = "limit" }) {
                    Text("Pending Order", modifier = Modifier.padding(12.dp))
                }
            }

            // 3. Inputs Section
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Quantity Field (Always visible)
                OrderInputField(
                    value = qty,
                    onValueChange = { qty = it },
                    label = "Quantity (Qty)",
                    placeholder = "0.00"
                )

                // Limit Price Field (Only for Pending Orders)
                if (orderType == "limit") {
                    OrderInputField(
                        value = limitPrice,
                        onValueChange = { limitPrice = it },
                        label = "Limit Price",
                        placeholder = "Enter target price"
                    )
                }

                // Time In Force Selector
                TIFSelector(timeInForce) { timeInForce = it }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 4. Execution Button
            Button(
                onClick = {
                    // Logic to build your JSON objects based on 'orderType'
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (side == "buy") MT5_UP else MT5_DOWN
                )
            ) {
                Text(
                    text = if (orderType == "market") "PLACE MARKET ORDER" else "PLACE PENDING ORDER",
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
    placeholder: String
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
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