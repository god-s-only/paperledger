package com.paperledger.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.paperledger.app.core.Routes
import com.paperledger.app.presentation.theme.PaperLedgerTheme
import com.paperledger.app.presentation.ui.features.ach_funding.ACHFundingScreen
import com.paperledger.app.presentation.ui.features.ach_relationships.ACHRelationShipScreen
import com.paperledger.app.presentation.ui.features.assets.AssetsScreen
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpScreen
import com.paperledger.app.presentation.ui.features.chart.FullTradeChartScreen
import com.paperledger.app.presentation.ui.features.funding.FundingScreen
import com.paperledger.app.presentation.ui.features.settings.SettingsScreen
import com.paperledger.app.presentation.ui.features.trade.PlaceTradeScreen
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
    val authViewModel: AuthViewModel = hiltViewModel()

    val authDestination by authViewModel.authDestination.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(authDestination) {
        val route = when (authDestination) {
            is AuthDestination.Loading -> return@LaunchedEffect
            is AuthDestination.SignUp -> Routes.SIGN_UP
            is AuthDestination.ACHRelationship -> Routes.ACH_RELATIONSHIP_SCREEN
            is AuthDestination.Funding -> Routes.FUNDING_SCREEN
            is AuthDestination.Watchlists -> Routes.WATCHLISTS_SCREEN
        }
        navController.navigate(route) {
            popUpTo(0) { inclusive = true } // Clear entire back stack
            launchSingleTop = true
        }
    }

    Scaffold(
        bottomBar = {
            val mainRoutes = listOf(
                Routes.WATCHLISTS_SCREEN,
                Routes.CHART_SCREEN,
                Routes.TRADE_SCREEN,
                Routes.SETTINGS_SCREEN
            )
            val shouldShowBottomBar = currentRoute in mainRoutes

            androidx.compose.animation.AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = androidx.compose.animation.slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = androidx.compose.animation.slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                PaperledgerBottomBar(navController, currentRoute)
            }
        }
    ) { innerPadding ->
        if (authDestination is AuthDestination.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        NavHost(
            navController = navController,
            startDestination = Routes.SIGN_UP,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
            composable(Routes.SIGN_UP) { SignUpScreen(navController = navController) }
            composable(Routes.ACH_RELATIONSHIP_SCREEN) { ACHRelationShipScreen() }
            composable(Routes.FUNDING_SCREEN) { FundingScreen(navController = navController) }
            composable(Routes.ASSETS_SCREEN) { AssetsScreen(navController = navController) }
            composable(Routes.WATCHLISTS_SCREEN) { WatchlistScreen(navController = navController) }
            composable(Routes.TRADE_SCREEN) { TradeScreen(navController = navController) }
            composable(Routes.CHART_SCREEN) { FullTradeChartScreen() }
            composable(Routes.SETTINGS_SCREEN) { SettingsScreen(navController = navController) }
            composable(Routes.ACH_FUNDING_SCREEN) { ACHFundingScreen(navController = navController) }
            composable(
                Routes.PLACE_TRADE_SCREEN + "?watchlistName={watchlistName}",
                arguments = listOf(
                    navArgument("watchlistName") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = "AAPL"
                    }
                )
            ) {
                PlaceTradeScreen(navController = navController)
            }
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

        // Tab 4: Settings
        NavigationBarItem(
            selected = currentRoute == Routes.SETTINGS_SCREEN,
            onClick = { navController.navigate(Routes.SETTINGS_SCREEN) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }},
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