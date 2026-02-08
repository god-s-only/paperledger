import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

@Composable
fun TradingViewChart(
    symbol: String = "BINANCE:BTCUSDT",
    interval: String = "D",
    theme: String = "dark",
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    setSupportZoom(false)
                    allowFileAccess = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                // Apply dark mode if supported
                if (WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
                    WebSettingsCompat.setAlgorithmicDarkeningAllowed(settings, theme == "dark")
                }

                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
            }
        },
        update = { webView ->
            val html = getTradingViewMiniHTML(symbol, interval, theme)
            webView.loadDataWithBaseURL(
                "https://www.tradingview.com/",
                html,
                "text/html",
                "UTF-8",
                null
            )
        },
        modifier = modifier
    )
}

// Most compatible TradingView widget
private fun getTradingViewMiniHTML(
    symbol: String,
    interval: String,
    theme: String
): String {
    val colorTheme = if (theme == "dark") "dark" else "light"

    return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body { margin: 0; padding: 0; overflow: hidden; }
        .tradingview-widget-container { height: 100vh; width: 100%; }
    </style>
</head>
<body>
    <div class="tradingview-widget-container">
        <div id="tradingview_widget"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
        <script type="text/javascript">
            new TradingView.MediumWidget({
                "symbols": [["$symbol"]],
                "chartOnly": false,
                "width": "100%",
                "height": "100%",
                "locale": "en",
                "colorTheme": "$colorTheme",
                "gridLineColor": "rgba(240, 243, 250, 0)",
                "trendLineColor": "rgba(41, 98, 255, 1)",
                "fontColor": "#787B86",
                "underLineColor": "rgba(41, 98, 255, 0.3)",
                "isTransparent": false,
                "autosize": true,
                "container_id": "tradingview_widget"
            });
        </script>
    </div>
</body>
</html>
    """.trimIndent()
}