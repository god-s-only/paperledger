package com.paperledger.app.presentation.ui.features.chart

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullTradeChartScreen(
    initialSymbol: String = "BINANCE:BTCUSDT",
    onBackClick: () -> Unit = {},
    onTradeClick: (String) -> Unit = {}
) {
    var currentSymbol by remember { mutableStateOf(initialSymbol) }
    var isDarkMode by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentSymbol.split(":").lastOrNull() ?: currentSymbol,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Live Market Feed", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                actions = {
                    IconButton(onClick = { isDarkMode = !isDarkMode }) {
                        Icon(Icons.Default.Settings, "Theme", tint = if (isDarkMode) Color.Yellow else Color.Gray)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(if (isDarkMode) Color.Black else Color.White)
        ) {
            TradingViewWebView(
                symbol = currentSymbol,
                isDarkMode = isDarkMode
            )

            FloatingActionButton(
                onClick = { onTradeClick(currentSymbol) },
                containerColor = MT5_BLUE.copy(alpha = 0.9f),
                contentColor = Color.White,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .size(width = 140.dp, height = 48.dp)
                    .align(Alignment.TopEnd)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("NEW ORDER", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold)
                }
            }

            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val pairs = listOf("BINANCE:BTCUSDT", "FX:EURUSD", "OANDA:XAUUSD", "NASDAQ:NVDA")
                    pairs.forEach { pair ->
                        val ticker = pair.split(":").last()
                        Button(
                            onClick = { currentSymbol = pair },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentSymbol == pair) MT5_BLUE else Color.Gray.copy(alpha = 0.1f),
                                contentColor = if (currentSymbol == pair) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(ticker, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
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
        <html>
            <head><meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>body { margin: 0; padding: 0; background-color: ${if (isDarkMode) "#000000" else "#FFFFFF"}; }</style></head>
            <body>
                <div id="tv_chart" style="height:100vh; width:100vw;"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
                <script type="text/javascript">
                new TradingView.widget({
                    "autosize": true, "symbol": "$symbol", "interval": "H1", "theme": "$theme",
                    "style": "1", "locale": "en", "enable_publishing": false,
                    "hide_top_toolbar": false, "save_image": false, "container_id": "tv_chart"
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
                    setSupportZoom(true)
                }
                webViewClient = WebViewClient()
                setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
                loadDataWithBaseURL("https://s3.tradingview.com", htmlData, "text/html", "UTF-8", null)
            }
        },
        update = { it.loadDataWithBaseURL("https://s3.tradingview.com", htmlData, "text/html", "UTF-8", null) }
    )
}