package com.paperledger.app.presentation.ui.features.watchlists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.paperledger.app.core.UIEvent
import com.paperledger.app.domain.models.watchlists.WatchlistsEntity

val MT5_BLUE = Color(0xFF2196F3)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchlistsScreenViewModel = hiltViewModel(),
    onWatchlistClick: (String) -> Unit,
    onAddWatchlist: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(event.message)
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "WATCHLISTS",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    },
                    actions = {
                        IconButton(onClick = onAddWatchlist) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = MT5_BLUE)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                // MT5 Style Linear Loading Bar
                if (state.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        color = MT5_BLUE,
                        trackColor = MT5_BLUE.copy(alpha = 0.1f)
                    )
                } else {
                    Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Priority 1: Error or Empty Message (Centered)
            if (state.message.isNotEmpty() || state.error != null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.error ?: state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = if (state.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (state.error != null) {
                        Button(
                            onClick = { viewModel.getWatchlists() },
                            modifier = Modifier.padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MT5_BLUE)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            // Priority 2: Data List
            else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.watchlists) { watchlist ->
                        WatchlistItem(
                            watchlist = watchlist,
                            onClick = { onWatchlistClick(watchlist.id) }
                        )
                        Divider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WatchlistItem(
    watchlist: WatchlistsEntity,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null,
                tint = MT5_BLUE,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = watchlist.name.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID: ${watchlist.id.take(8)}...",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
        Text(
            text = "➔",
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    WatchlistScreen(onWatchlistClick = {}, onAddWatchlist = {})
}