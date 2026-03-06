package com.paperledger.app.presentation.ui.features.chart

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paperledger.app.core.UIEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val MT5_BLUE = Color(0xFF2196F3)
val MT5_UP = Color(0xFF4CAF50)
val MT5_DOWN = Color(0xFFF44336)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullTradeChartScreen(
    initialSymbol: String = "BTCUSDT",
    viewModel: FullTradeChartViewModel = hiltViewModel()
) {
    var isDarkMode by remember { mutableStateOf(true) }
    var showQuickTrade by remember { mutableStateOf(false) }
    var selectedSymbol by remember { mutableStateOf(initialSymbol) }
    var showSymbolDropdown by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { result ->
            when(result){
                is UIEvent.ShowSnackBar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(result.message)
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { showSymbolDropdown = true }
                                    .padding(8.dp)
                            ) {
                                // Use state.symbol instead of a local variable
                                Text(
                                    text = state.value.symbol,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showSymbolDropdown,
                                onDismissRequest = { showSymbolDropdown = false },
                                modifier = Modifier.widthIn(min = 200.dp)
                            ) {
                                if (state.value.watchlists.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("No assets available", color = Color.Gray) },
                                        onClick = { showSymbolDropdown = false }
                                    )
                                } else {
                                    state.value.watchlists.forEach { watchlist ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = watchlist.name.uppercase(),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            },
                                            onClick = {
                                                viewModel.onEvent(FullTradeChartEvent.OnSymbolChange(watchlist.name))
                                                showSymbolDropdown = false
                                            },
                                            trailingIcon = {
                                                if (watchlist.name == state.value.symbol) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = MT5_BLUE,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { showQuickTrade = !showQuickTrade }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Quick Trade",
                                tint = if (showQuickTrade) MT5_BLUE else LocalContentColor.current
                            )
                        }
                        IconButton(onClick = { isDarkMode = !isDarkMode }) {
                            Icon(
                                Icons.Default.Settings,
                                "Theme",
                                tint = if (isDarkMode) Color.Yellow else Color.Gray
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                AnimatedVisibility(
                    visible = showQuickTrade,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    QuickTradePanel(
                        currentSymbol = state.value.symbol,
                        currentQty = state.value.qty,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(if (isDarkMode) Color.Black else Color.White)
        ) {
            TradingViewWebView(
                symbol = "BINANCE:$selectedSymbol",
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
fun QuickTradePanel(
    currentSymbol: String,
    currentQty: String,
    onEvent: (FullTradeChartEvent) -> Unit,
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(50.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // SELL Button
            Button(
                onClick = { onEvent(FullTradeChartEvent.OnTradeClick("sell")) },
                colors = ButtonDefaults.buttonColors(containerColor = MT5_DOWN),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SELL", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("Market", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                }
            }

            // QTY Input (Compact)
            OutlinedTextField(
                value = currentQty,
                onValueChange = { onEvent(FullTradeChartEvent.OnQtyChange(qty = it)) },
                modifier = Modifier
                    .weight(0.7f)
                    .height(45.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                placeholder = { Text("Qty", fontSize = 10.sp) },
                shape = RoundedCornerShape(4.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MT5_BLUE,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                )
            )

            // Symbol Selection/Input
            OutlinedTextField(
                value = currentSymbol,
                onValueChange = { onEvent(FullTradeChartEvent.OnSymbolChange(symbol = it)) },
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                shape = RoundedCornerShape(4.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MT5_BLUE,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                )
            )

            // BUY Button
            Button(
                onClick = { onEvent(FullTradeChartEvent.OnTradeClick("buy")) },
                colors = ButtonDefaults.buttonColors(containerColor = MT5_UP),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BUY", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("Market", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TradingViewWebView(
    symbol: String,
    isDarkMode: Boolean
) {
    val theme = if (isDarkMode) "dark" else "light"
    val htmlData = remember(symbol, isDarkMode) {
        """
        <!DOCTYPE html>
        <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <style>
                    body { margin: 0; padding: 0; background-color: ${if (isDarkMode) "#000000" else "#FFFFFF"}; }
                    #tv_container { height: 100vh; width: 100vw; }
                </style>
            </head>
            <body>
                <div id="tv_container"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
                <script type="text/javascript">
                new TradingView.widget({
                    "autosize": true,
                    "symbol": "$symbol",
                    "interval": "D",
                    "timezone": "Etc/UTC",
                    "theme": "$theme",
                    "style": "1",
                    "locale": "en",
                    "enable_publishing": false,
                    "allow_symbol_change": true, // ENABLES SYMBOL SEARCH
                    "hide_top_toolbar": false,   // SHOWS THE TOOLBAR WITH SYMBOL NAME
                    "hide_side_toolbar": false,  // SHOWS DRAWING TOOLS
                    "withdateranges": true,
                    "save_image": false,
                    "container_id": "tv_container"
                });
                </script>
            </body>
        </html>
        """.trimIndent()
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }
                webViewClient = WebViewClient()
                loadDataWithBaseURL("https://s3.tradingview.com", htmlData, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL("https://s3.tradingview.com", htmlData, "text/html", "UTF-8", null)
        }
    )
}