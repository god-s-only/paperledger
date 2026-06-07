package com.paperledger.app.presentation.ui.features.funding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.paperledger.app.core.UIEvent
import com.paperledger.app.presentation.ui.features.ach_relationships.MT5InputField
import com.paperledger.app.presentation.ui.features.trade.MT5_BLUE

@Composable
fun FundingScreen(
    viewModel: FundingScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> {
                    navController.navigate(event.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
                is UIEvent.PopBackStack -> {
                    navController.popBackStack()
                }
                is UIEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                val isError = !snackbarData.visuals.message.startsWith("Transfer initiated")
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = if (isError) Color(0xFFB71C1C) else Color(0xFF388E3C),
                    contentColor = Color.White
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MT5_BLUE,
                    trackColor = MT5_BLUE.copy(alpha = 0.1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
                    .alpha(if (state.isLoading) 0.6f else 1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                FundingHeader(direction = state.direction)

                MT5InputField(
                    value = state.transferType.uppercase(),
                    onValueChange = { },
                    label = "Transfer Method",
                    enabled = false
                )

                MT5InputField(
                    value = state.relationshipId,
                    onValueChange = { },
                    label = "Relationship ID",
                    placeholder = "Loading...",
                    enabled = false
                )

                OutlinedTextField(
                    value = state.amount,
                    onValueChange = { viewModel.onEvent(FundingScreenEvent.OnAmountChange(amount = it)) },
                    label = { Text("Amount") },
                    prefix = { Text("$", color = MT5_BLUE, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MT5_BLUE,
                        focusedLabelColor = MT5_BLUE,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.onEvent(FundingScreenEvent.OnSubmit) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MT5_BLUE,
                        disabledContainerColor = MT5_BLUE.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        val isIncoming = state.direction.equals("INCOMING", ignoreCase = true)
                        Text(
                            text = if (isIncoming) "CONFIRM DEPOSIT" else "CONFIRM WITHDRAWAL",
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FundingHeader(direction: String) {
    val isIncoming = direction.equals("INCOMING", ignoreCase = true)
    Column {
        Text(
            text = if (isIncoming) "DEPOSIT FUNDS" else "WITHDRAW FUNDS",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = if (isIncoming) "External Account ➔ Trading Account" else "Trading Account ➔ External Account",
            style = MaterialTheme.typography.labelSmall,
            color = if (isIncoming) Color(0xFF4CAF50) else Color(0xFFF44336),
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    FundingScreen(navController = rememberNavController())
}