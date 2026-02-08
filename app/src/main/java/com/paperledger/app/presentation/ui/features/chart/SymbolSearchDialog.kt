import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class SymbolSuggestion(
    val symbol: String,
    val name: String,
    val exchange: String,
    val type: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymbolSearchDialog(
    currentSymbol: String,
    onSymbolSelected: (symbol: String, name: String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var customSymbol by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search Symbols",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search stocks, crypto, forex...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Symbol Input Toggle
                TextButton(
                    onClick = { showCustomInput = !showCustomInput },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (showCustomInput) Icons.Default.KeyboardArrowUp else Icons.Default.Edit,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enter custom TradingView symbol")
                }

                // Custom Symbol Input
                if (showCustomInput) {
                    OutlinedTextField(
                        value = customSymbol,
                        onValueChange = { customSymbol = it.uppercase() },
                        label = { Text("e.g., NASDAQ:AAPL, BINANCE:BTCUSDT") },
                        placeholder = { Text("EXCHANGE:SYMBOL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            if (customSymbol.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        onSymbolSelected(customSymbol, customSymbol)
                                    }
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = "Use symbol")
                                }
                            }
                        }
                    )
                    Text(
                        text = "Format: EXCHANGE:SYMBOL (e.g., NASDAQ:TSLA)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Results
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val filteredSymbols = getSymbolSuggestions().filter { symbol ->
                        searchQuery.isEmpty() ||
                                symbol.name.contains(searchQuery, ignoreCase = true) ||
                                symbol.symbol.contains(searchQuery, ignoreCase = true) ||
                                symbol.exchange.contains(searchQuery, ignoreCase = true)
                    }

                    if (searchQuery.isEmpty()) {
                        item {
                            Text(
                                text = "Popular Symbols",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    items(filteredSymbols) { suggestion ->
                        SymbolListItem(
                            suggestion = suggestion,
                            isSelected = currentSymbol == suggestion.symbol,
                            onClick = {
                                onSymbolSelected(suggestion.symbol, suggestion.name)
                            }
                        )
                    }

                    if (filteredSymbols.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No results found",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Try entering a custom symbol above",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SymbolListItem(
    suggestion: SymbolSuggestion,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = suggestion.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = suggestion.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = suggestion.type,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    text = suggestion.exchange,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

fun getSymbolSuggestions(): List<SymbolSuggestion> {
    return listOf(
        // Crypto
        SymbolSuggestion("BINANCE:BTCUSDT", "Bitcoin", "Binance", "Crypto"),
        SymbolSuggestion("BINANCE:ETHUSDT", "Ethereum", "Binance", "Crypto"),
        SymbolSuggestion("BINANCE:BNBUSDT", "BNB", "Binance", "Crypto"),
        SymbolSuggestion("BINANCE:SOLUSDT", "Solana", "Binance", "Crypto"),
        SymbolSuggestion("BINANCE:XRPUSDT", "Ripple", "Binance", "Crypto"),
        SymbolSuggestion("BINANCE:ADAUSDT", "Cardano", "Binance", "Crypto"),
        SymbolSuggestion("BINANCE:DOGEUSDT", "Dogecoin", "Binance", "Crypto"),

        // US Stocks
        SymbolSuggestion("NASDAQ:AAPL", "Apple Inc", "NASDAQ", "Stock"),
        SymbolSuggestion("NASDAQ:MSFT", "Microsoft", "NASDAQ", "Stock"),
        SymbolSuggestion("NASDAQ:GOOGL", "Alphabet", "NASDAQ", "Stock"),
        SymbolSuggestion("NASDAQ:AMZN", "Amazon", "NASDAQ", "Stock"),
        SymbolSuggestion("NASDAQ:TSLA", "Tesla", "NASDAQ", "Stock"),
        SymbolSuggestion("NASDAQ:META", "Meta", "NASDAQ", "Stock"),
        SymbolSuggestion("NASDAQ:NVDA", "NVIDIA", "NASDAQ", "Stock"),

        // Forex
        SymbolSuggestion("FX:EURUSD", "EUR/USD", "Forex", "Forex"),
        SymbolSuggestion("FX:GBPUSD", "GBP/USD", "Forex", "Forex"),
        SymbolSuggestion("FX:USDJPY", "USD/JPY", "Forex", "Forex"),

        // Indices
        SymbolSuggestion("SP:SPX", "S&P 500", "S&P", "Index"),
        SymbolSuggestion("NASDAQ:IXIC", "NASDAQ", "NASDAQ", "Index"),
    )
}