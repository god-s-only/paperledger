import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TradingViewChart(
    symbol: String = "BINANCE:BTCUSDT",
    interval: String = "D",
    theme: String = "dark",
    modifier: Modifier = Modifier
) {
    var webView by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(symbol, interval, theme) {
        webView?.let {
            val html = getTradingViewHTML(symbol, interval, theme)
            it.loadDataWithBaseURL(
                "https://www.tradingview.com",
                html,
                "text/html",
                "UTF-8",
                null
            )
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }

                webView = this

                val html = getTradingViewHTML(symbol, interval, theme)
                loadDataWithBaseURL(
                    "https://www.tradingview.com",
                    html,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        update = { view ->
            // This runs when composition updates
            val html = getTradingViewHTML(symbol, interval, theme)
            view.loadDataWithBaseURL(
                "https://www.tradingview.com",
                html,
                "text/html",
                "UTF-8",
                null
            )
        },
        modifier = modifier.fillMaxSize()
    )
}

private fun getTradingViewHTML(
    symbol: String,
    interval: String,
    theme: String
): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body { 
                    margin: 0; 
                    padding: 0; 
                    overflow: hidden;
                }
                #tradingview_chart { 
                    height: 100vh; 
                    width: 100%; 
                }
            </style>
        </head>
        <body>
            <div id="tradingview_chart"></div>
            <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
            <script type="text/javascript">
                new TradingView.widget({
                    "autosize": true,
                    "symbol": "$symbol",
                    "interval": "$interval",
                    "timezone": "Etc/UTC",
                    "theme": "$theme",
                    "style": "1",
                    "locale": "en",
                    "toolbar_bg": "#f1f3f6",
                    "enable_publishing": false,
                    "allow_symbol_change": true,
                    "hide_side_toolbar": false,
                    "container_id": "tradingview_chart"
                });
            </script>
        </body>
        </html>
    """.trimIndent()
}