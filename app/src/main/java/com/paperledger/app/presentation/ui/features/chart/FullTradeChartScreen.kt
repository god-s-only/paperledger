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

val MT5_BLUE = Color(0xFF2196F3)
val MT5_UP = Color(0xFF4CAF50)
val MT5_DOWN = Color(0xFFF44336)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullTradeChartScreen(
    initialSymbol: String = "BTCUSDT",
    onBackClick: () -> Unit = {},
    onTradeEvent: (side: String, symbol: String) -> Unit = { _, _ -> }
) {
    var isDarkMode by remember { mutableStateOf(true) }
    var showQuickTrade by remember { mutableStateOf(false) }
    var selectedSymbol by remember { mutableStateOf(initialSymbol) }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { /* Toggle symbol search */ }
                        ) {
                            Text(selectedSymbol, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            Icon(Icons.Default.KeyboardArrowDown, null, Modifier.size(20.dp))
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                    },
                    actions = {
                        // The "Plus" icon that reveals the trade bar
                        IconButton(onClick = { showQuickTrade = !showQuickTrade }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Quick Trade",
                                tint = if (showQuickTrade) MT5_BLUE else LocalContentColor.current
                            )
                        }
                        IconButton(onClick = { isDarkMode = !isDarkMode }) {
                            Icon(Icons.Default.Settings, "Theme", tint = if (isDarkMode) Color.Yellow else Color.Gray)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                // MT5-style Quick Trade Panel
                AnimatedVisibility(
                    visible = showQuickTrade,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    QuickTradePanel(
                        currentSymbol = selectedSymbol,
                        onSymbolChange = { selectedSymbol = it },
                        onTradeClick = { side -> onTradeEvent(side, selectedSymbol) }
                    )
                }
            }
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
    onSymbolChange: (String) -> Unit,
    onTradeClick: (String) -> Unit
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // SELL Button
            Button(
                onClick = { onTradeClick("sell") },
                colors = ButtonDefaults.buttonColors(containerColor = MT5_DOWN),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SELL", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("Market", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
            }

            // Symbol Selection/Input
            OutlinedTextField(
                value = currentSymbol,
                onValueChange = onSymbolChange,
                modifier = Modifier.weight(1.2f).height(45.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                shape = RoundedCornerShape(4.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MT5_BLUE,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                )
            )

            // BUY Button
            Button(
                onClick = { onTradeClick("buy") },
                colors = ButtonDefaults.buttonColors(containerColor = MT5_UP),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BUY", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("Market", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

// ... Keep your TradingViewWebView as it was ...

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