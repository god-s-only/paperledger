package com.paperledger.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paperledger.app.core.Routes
import com.paperledger.app.presentation.theme.PaperLedgerTheme
import com.paperledger.app.presentation.ui.features.ach_relationships.ACHRelationShipScreen
import com.paperledger.app.presentation.ui.features.assets.AssetsScreen
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpScreen
import com.paperledger.app.presentation.ui.features.chart.FullTradeChartScreen
import com.paperledger.app.presentation.ui.features.funding.FundingScreen
import com.paperledger.app.presentation.ui.features.trade.TradeScreen
import com.paperledger.app.presentation.ui.features.watchlists.WatchlistScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaperLedgerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val mainRoutes = listOf(Routes.WATCHLISTS_SCREEN, Routes.CHART_SCREEN, Routes.TRADE_SCREEN)
            if (currentRoute in mainRoutes || currentRoute == "settings") {
                PaperledgerBottomBar(navController, currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.CHART_SCREEN,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(Routes.SIGN_UP) { SignUpScreen(navController = navController) }
            composable(Routes.ACH_RELATIONSHIP_SCREEN) { ACHRelationShipScreen() }
            composable(Routes.FUNDING_SCREEN) { FundingScreen(navController = navController) }
            composable(Routes.ASSETS_SCREEN) { AssetsScreen(navController = navController) }
            composable(Routes.WATCHLISTS_SCREEN) { WatchlistScreen(navController = navController) }
            composable(Routes.TRADE_SCREEN) { TradeScreen(navController = navController) }
            composable(Routes.CHART_SCREEN) { FullTradeChartScreen() }
            composable("settings") { /* Placeholder for Settings */ }
        }
    }
}

@Composable
fun PaperledgerBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == Routes.WATCHLISTS_SCREEN,
            onClick = { navController.navigate(Routes.WATCHLISTS_SCREEN) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }},
            label = { Text("Watchlists", fontSize = 10.sp) },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
            colors = navigationBarItemColors()
        )

        // Tab 2: Chart
        NavigationBarItem(
            selected = currentRoute == Routes.CHART_SCREEN,
            onClick = { navController.navigate(Routes.CHART_SCREEN) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }},
            label = { Text("Charts", fontSize = 10.sp) },
            icon = { Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = null) },
            colors = navigationBarItemColors()
        )

        // Tab 3: Trade
        NavigationBarItem(
            selected = currentRoute == Routes.TRADE_SCREEN,
            onClick = { navController.navigate(Routes.TRADE_SCREEN) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }},
            label = { Text("Trade", fontSize = 10.sp) },
            icon = { Icon(Icons.Default.SwapHoriz, contentDescription = null) },
            colors = navigationBarItemColors()
        )

        // Tab 4: Settings (No route yet)
        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = { /* Do nothing as requested */ },
            label = { Text("Settings", fontSize = 10.sp) },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            colors = navigationBarItemColors()
        )
    }
}

@Composable
fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color(0xFF2196F3), // MT5 Blue
    selectedTextColor = Color(0xFF2196F3),
    unselectedIconColor = Color.Gray,
    unselectedTextColor = Color.Gray,
    indicatorColor = MaterialTheme.colorScheme.surface // Removes the pill-shaped background
)