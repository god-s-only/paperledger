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
import com.paperledger.app.presentation.ui.features.ach_relationships.MT5InputField

val MT5_BLUE = Color(0xFF2196F3)

@Composable
fun FundingScreen(
    initialDirection: String = "INCOMING",
    initialRelationshipId: String = "2607a7b8-cf50-4c9c-a107-19475e162ae6",
    onConfirm: (amount: String, relationshipId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Internal State Management
    var amount by remember { mutableStateOf("1234.56") }
    var relationshipId by remember { mutableStateOf(initialRelationshipId) }
    var transferType by remember { mutableStateOf("ach") }
    var direction by remember { mutableStateOf(initialDirection) }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (isLoading) {
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
                    .alpha(if (isLoading) 0.6f else 1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                FundingHeader(direction = direction)

                // Transfer Method (Locked)
                MT5InputField(
                    value = transferType.uppercase(),
                    onValueChange = { transferType = it },
                    label = "Transfer Method",
                    enabled = false
                )

                // Relationship ID Input
                MT5InputField(
                    value = relationshipId,
                    onValueChange = { relationshipId = it },
                    label = "Relationship ID",
                    placeholder = "Enter Relationship UUID",
                    enabled = false
                )

                // Large Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    prefix = { Text("$", color = MT5_BLUE, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isLoading,
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

                // Action Button
                Button(
                    onClick = {
                        onConfirm(amount, relationshipId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MT5_BLUE,
                        disabledContainerColor = MT5_BLUE.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        val isIncoming = direction.equals("INCOMING", ignoreCase = true)
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
    FundingScreen(onConfirm = {_,_ ->})
}