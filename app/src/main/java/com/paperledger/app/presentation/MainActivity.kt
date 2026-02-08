package com.paperledger.app.presentation

import ChartScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paperledger.app.core.Routes
import com.paperledger.app.presentation.theme.PaperLedgerTheme
import com.paperledger.app.presentation.ui.features.ach_relationships.ACHRelationShipScreen
import com.paperledger.app.presentation.ui.features.assets.AssetsScreen
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpScreen
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
    NavHost(navController, startDestination = Routes.CHART_SCREEN){
        composable(Routes.SIGN_UP) {
            SignUpScreen(navController = navController)
        }
        composable(Routes.ACH_RELATIONSHIP_SCREEN) {
            ACHRelationShipScreen()
        }
        composable(Routes.FUNDING_SCREEN) {
            FundingScreen(navController = navController)
        }
        composable(Routes.ASSETS_SCREEN) {
            AssetsScreen(navController = navController)
        }
        composable(Routes.WATCHLISTS_SCREEN) {
            WatchlistScreen(navController = navController)
        }
        composable(Routes.TRADE_SCREEN) {
            TradeScreen(navController = navController)
        }
        composable(Routes.CHART_SCREEN) {
            ChartScreen()
        }

    }
}