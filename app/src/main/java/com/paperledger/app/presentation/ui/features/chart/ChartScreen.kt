import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen() {
    var currentSymbol by remember { mutableStateOf("BINANCE:BTCUSDT") }
    var currentSymbolName by remember { mutableStateOf("Bitcoin") }
    var interval by remember { mutableStateOf("D") }
    var theme by remember { mutableStateOf("dark") }
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Trading Chart")
                        Text(
                            text = currentSymbolName,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { theme = if (theme == "dark") "light" else "dark" }
                    ) {
                        Icon(
                            imageVector = if (theme == "dark") Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme"
                        )
                    }

                    IconButton(onClick = { showSearch = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Search Symbol")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Interval selector
            IntervalSelector(
                selectedInterval = interval,
                onIntervalChange = { interval = it }
            )

            // TradingView Chart
            TradingViewChart(
                symbol = currentSymbol,
                interval = interval,
                theme = theme,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }

    // Search Dialog
    if (showSearch) {
        SymbolSearchDialog(
            currentSymbol = currentSymbol,
            onSymbolSelected = { symbol, name ->
                currentSymbol = symbol
                currentSymbolName = name
                showSearch = false
            },
            onDismiss = { showSearch = false }
        )
    }
}

@Composable
fun IntervalSelector(
    selectedInterval: String,
    onIntervalChange: (String) -> Unit
) {
    val intervals = listOf(
        "1" to "1m",
        "5" to "5m",
        "15" to "15m",
        "30" to "30m",
        "60" to "1h",
        "240" to "4h",
        "D" to "1D",
        "W" to "1W",
        "M" to "1M"
    )

    Surface(
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            intervals.forEach { (value, label) ->
                FilterChip(
                    selected = selectedInterval == value,
                    onClick = { onIntervalChange(value) },
                    label = { Text(label) }
                )
            }
        }
    }
}