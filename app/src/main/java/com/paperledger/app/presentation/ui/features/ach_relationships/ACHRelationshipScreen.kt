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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

val MT5_BLUE = Color(0xFF2196F3)

@Composable
fun ACHRelationShipScreen(
    modifier: Modifier = Modifier,
    viewModel: ACHRelationshipViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "PAYMENT DETAILS",
            style = MaterialTheme.typography.labelLarge,
            color = MT5_BLUE,
            fontWeight = FontWeight.Bold
        )

        // Nickname Input
        MT5InputField(
            value = state.value.nickname,
            onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnNickNameChange(it))},
            label = "Nickname",
            placeholder = "e.g. Primary Bank"
        )

        // Account Owner Input
        MT5InputField(
            value = state.value.ownerName,
            onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnOwnerNameChange(it)) },
            label = "Account Owner Name"
        )

        // Bank Account Type (Dropdown or TextField)
        MT5InputField(
            value = state.value.accountType,
            onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnAccountTypeChange(it)) },
            label = "Bank Account Type"
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Account Number
            Box(modifier = Modifier.weight(1f)) {
                MT5InputField(
                    value = state.value.accountNumber,
                    onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnAccountNumberChange(it)) },
                    label = "Account Number"
                )
            }
            // Routing Number
            Box(modifier = Modifier.weight(1f)) {
                MT5InputField(
                    value = state.value.routingNumber,
                    onValueChange = { viewModel.onEvent(ACHRelationshipEvent.OnRoutingNumberChange(it)) },
                    label = "Routing Number"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.onEvent(ACHRelationshipEvent.OnSubmit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MT5_BLUE),
            shape = MaterialTheme.shapes.small
        ) {
            Text("SAVE CHANGES", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MT5InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = value,
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