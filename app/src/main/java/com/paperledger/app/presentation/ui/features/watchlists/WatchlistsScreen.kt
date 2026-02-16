package com.paperledger.app.presentation.ui.features.watchlists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.paperledger.app.core.UIEvent
import com.paperledger.app.data.local.WatchlistsEntity

val MT5_BLUE = Color(0xFF2196F3)
val MT5_DOWN = Color(0xFFF44336)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchlistsScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Bottom Sheet State
    var selectedWatchlist by remember { mutableStateOf<WatchlistsEntity?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(event.message)
                is UIEvent.Navigate -> navController.navigate(event.route)
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
                        IconButton(onClick = { viewModel.onEvent(WatchlistsAction.OnAddWatchlistClick) }) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = MT5_BLUE)
                        }
                    }
                )
                if (state.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().height(2.dp),
                        color = MT5_BLUE,
                        trackColor = MT5_BLUE.copy(alpha = 0.1f)
                    )
                } else {
                    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
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
            if (state.message.isNotEmpty()) {
                Text(
                    text = state.message,
                    modifier = Modifier.align(Alignment.Center).padding(32.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.watchlists) { watchlist ->
                        WatchlistItem(
                            watchlist = watchlist,
                            onClick = {
                                selectedWatchlist = watchlist
                                showSheet = true
                            }
                        )
                        HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.1f))
                    }
                }
            }

            // Bottom Sheet Implementation
            if (showSheet && selectedWatchlist != null) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    WatchlistActionContent(
                        watchlist = selectedWatchlist!!,
                        onTrade = {
                            showSheet = false
                            viewModel.onEvent(WatchlistsAction.OnWatchlistClick(selectedWatchlist!!))
                        },
                        onDelete = {
                            showSheet = false

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WatchlistActionContent(
    watchlist: WatchlistsEntity,
    onTrade: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, top = 8.dp)
    ) {
        Text(
            text = watchlist.name.uppercase(),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = MT5_BLUE
        )

        ListItem(
            headlineContent = { Text("Trade Asset", fontWeight = FontWeight.SemiBold) },
            leadingContent = { Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = MT5_BLUE) },
            modifier = Modifier.clickable { onTrade() }
        )

        ListItem(
            headlineContent = { Text("Delete Watchlist", color = MT5_DOWN, fontWeight = FontWeight.SemiBold) },
            leadingContent = { Icon(Icons.Default.Delete, contentDescription = null, tint = MT5_DOWN) },
            modifier = Modifier.clickable { onDelete() }
        )
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
        Text("➔", color = Color.Gray.copy(alpha = 0.4f), fontSize = 18.sp)
    }
}