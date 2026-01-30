package com.paperledger.app.presentation.ui.features.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val MT5_BLUE = Color(0xFF2196F3)
val DARK_GREY = Color(0xFF1E222D)

@Composable
fun AssetsScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val assets = remember { assetDataJson }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "MARKET WATCH",
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MT5_BLUE,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search symbols...", fontSize = 14.sp) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(24.dp)) },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MT5_BLUE,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        )

        Divider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        // --- Assets List ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(assets.filter { it.symbol.contains(searchQuery, ignoreCase = true) }) { asset ->
                AssetRow(asset)
                Divider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun AssetRow(asset: AssetInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = asset.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(asset.status)
            }
            Text(
                text = if (asset.name.isBlank()) "Unknown Asset" else asset.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = asset.exchange,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MT5_BLUE
            )
            Text(
                text = asset.assetClass.replace("_", " ").uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = if (status == "active") Color(0xFF4CAF50) else Color(0xFFF44336)
    Box(
        modifier = Modifier
            .border(0.5.dp, color.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 4.dp, vertical = 1.dp)
    ) {
        Text(
            text = status.uppercase(),
            fontSize = 9.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

data class AssetInfo(
    val symbol: String,
    val name: String,
    val exchange: String,
    val status: String,
    val assetClass: String
)

// Simplified list for the example
val assetDataJson = listOf(
    AssetInfo("292RGT016", "Empire Petroleum Corp. Rights", "NASDAQ", "inactive", "us_equity"),
    AssetInfo("IGGHY", "IG Group Holdings Plc", "OTC", "inactive", "us_equity"),
    AssetInfo("BCNA", "", "NASDAQ", "inactive", "us_equity"),
    AssetInfo("LVUS", "Hartford Multifactor Low Volatility", "BATS", "inactive", "us_equity")
)

@Preview
@Composable
private fun DefaultPreview() {
    AssetsScreen()
}