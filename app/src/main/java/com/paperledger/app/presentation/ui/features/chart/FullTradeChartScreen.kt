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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullTradeChartScreen(
    initialSymbol: String = "BINANCE:BTCUSDT",
    onBackClick: () -> Unit = {},
    onTradeClick: () -> Unit = {}
) {
    var isDarkMode by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chart", fontWeight = FontWeight.Bold) },
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
                symbol = initialSymbol,
                isDarkMode = isDarkMode
            )

            FloatingActionButton(
                onClick = onTradeClick,
                containerColor = Color(0xFF2196F3).copy(alpha = 0.95f),
                contentColor = Color.White,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .width(150.dp)
                    .height(50.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("NEW ORDER", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
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