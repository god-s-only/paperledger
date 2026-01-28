package com.paperledger.app.presentation.ui.features.ach_relationships

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.paperledger.app.core.UIEvent

val MT5_BLUE = Color(0xFF2196F3)

@Composable
fun ACHRelationShipScreen(
    modifier: Modifier = Modifier,
    viewModel: ACHRelationshipViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is UIEvent.Navigate -> {
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                    color = MT5_BLUE,
                    trackColor = MT5_BLUE.copy(alpha = 0.1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
                    .alpha(if (state.isLoading) 0.5f else 1f), // Dim UI while loading
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "PAYMENT DETAILS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MT5_BLUE,
                    fontWeight = FontWeight.Bold
                )

                MT5InputField(
                    value = state.nickname,
                    onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnNickNameChange(it)) },
                    label = "Nickname",
                    enabled = !state.isLoading
                )

                MT5InputField(
                    value = state.ownerName,
                    onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnOwnerNameChange(it)) },
                    label = "Account Owner Name",
                    enabled = !state.isLoading
                )

                MT5InputField(
                    value = state.accountType,
                    onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnAccountTypeChange(it)) },
                    label = "Bank Account Type",
                    enabled = !state.isLoading
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        MT5InputField(
                            value = state.accountNumber,
                            onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnAccountNumberChange(it)) },
                            label = "Account Number",
                            enabled = !state.isLoading
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        MT5InputField(
                            value = state.routingNumber,
                            onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnRoutingNumberChange(it)) },
                            label = "Routing Number",
                            enabled = !state.isLoading
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.onEvent(ACHRelationshipEvent.OnSubmit) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MT5_BLUE,
                        disabledContainerColor = MT5_BLUE.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("SAVE CHANGES", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MT5InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        enabled = enabled,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MT5_BLUE,
            focusedLabelColor = MT5_BLUE,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            cursorColor = MT5_BLUE
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
private fun Default() {
    ACHRelationShipScreen()
}