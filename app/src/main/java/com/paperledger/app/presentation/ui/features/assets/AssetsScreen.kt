package com.paperledger.app.presentation.ui.features.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.paperledger.app.core.UIEvent
import com.paperledger.app.domain.models.assets.AssetsModel
import kotlinx.coroutines.flow.collectLatest

val MT5_BLUE = Color(0xFF2196F3)
val MT5_GREEN = Color(0xFF4CAF50)
val MT5_RED = Color(0xFFF44336)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetsScreen(
    viewModel: AssetsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedAsset by remember { mutableStateOf<AssetsModel?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        withDismissAction = true
                    )
                }
                is UIEvent.Navigate -> {
                    navController.navigate(event.route)
                }
                is UIEvent.PopBackStack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. High-Precision Loading Bar (MT5 Style)
            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MT5_BLUE,
                    trackColor = MT5_BLUE.copy(alpha = 0.1f)
                )
            } else {
                Spacer(modifier = Modifier.height(2.dp))
            }

            // 2. Section Header
            Text(
                text = "MARKET WATCH",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MT5_BLUE,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            // 3. Search Input Field
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onEvent(AssetsScreenEvent.OnSearchQueryChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search symbols...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MT5_BLUE,
                    focusedLabelColor = MT5_BLUE,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            )

            Divider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            // 4. Content Area
            Box(modifier = Modifier.fillMaxSize()) {

                // Asset List
                val filteredAssets = remember(state.assets, state.searchQuery) {
                    state.assets.filter {
                        it.symbol.contains(state.searchQuery, ignoreCase = true) ||
                                it.name.contains(state.searchQuery, ignoreCase = true)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (state.isLoading && filteredAssets.isNotEmpty()) 0.5f else 1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(items = filteredAssets) { asset ->
                        AssetRow(
                            asset = asset,
                            onClick = {
                                selectedAsset = asset
                            }
                        )
                        Divider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                // Initial Loading State (Large Spinner)
                if (state.isLoading && state.assets.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MT5_BLUE,
                        strokeWidth = 3.dp
                    )
                }

                // Empty State Feedback
                if (!state.isLoading && filteredAssets.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No assets found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (state.searchQuery.isNotEmpty()) {
                            Text(
                                text = "Try adjusting your search",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Asset Bottom Sheet
                selectedAsset?.let { asset ->
                    ModalBottomSheet(
                        onDismissRequest = { selectedAsset = null },
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Asset Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            MT5_BLUE.copy(alpha = 0.1f),
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = asset.symbol.take(2),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MT5_BLUE
                                    )
                                }
                                Column {
                                    Text(
                                        text = asset.symbol,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = asset.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Divider(
                                modifier = Modifier.padding(vertical = 16.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )

                            // Information Button
                            Button(
                                onClick = {
                                    selectedAsset = null
                                    viewModel.onEvent(AssetsScreenEvent.OnAssetClick(asset))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = "Information",
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Information",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Add to Watchlist Button
                    Button(
                        onClick = {
                            selectedAsset = null
                            viewModel.onEvent(AssetsScreenEvent.OnCreateWatchlist(asset))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MT5_BLUE
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Add to Watchlist",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Add to Watchlist",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AssetRow(asset: AssetsModel, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Column: Ticker and Full Name
        Column(modifier = Modifier.weight(1.2f)) {
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
                text = asset.name.ifBlank { "Unlisted Security" },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Right Column: Exchange and Asset Class
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(0.8f)) {
            Text(
                text = asset.exchange,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MT5_BLUE,
                textAlign = TextAlign.End
            )
            Text(
                text = asset.assetClass.replace("_", " ").uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val isActive = status.equals("active", ignoreCase = true)
    val color = if (isActive) MT5_GREEN else MT5_RED

    Box(
        modifier = Modifier
            .border(0.5.dp, color.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = status.uppercase(),
            fontSize = 9.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssetsPreview() {
    AssetsScreen(navController = rememberNavController())
}