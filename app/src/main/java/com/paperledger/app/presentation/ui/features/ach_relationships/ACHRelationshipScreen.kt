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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val MT5_BLUE = Color(0xFF2196F3)

@Composable
fun ACHRelationShipScreen(modifier: Modifier = Modifier) {
    var nickname by remember { mutableStateOf("Bank of America Checking") }
    var ownerName by remember { mutableStateOf("Eloquent Fermat") }
    var accountType by remember { mutableStateOf("CHECKING") }
    var accountNumber by remember { mutableStateOf("32131231abc") }
    var routingNumber by remember { mutableStateOf("123103716") }

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
            value = nickname,
            onValueChange = { nickname = it },
            label = "Nickname",
            placeholder = "e.g. Primary Bank"
        )

        // Account Owner Input
        MT5InputField(
            value = ownerName,
            onValueChange = { ownerName = it },
            label = "Account Owner Name"
        )

        // Bank Account Type (Dropdown or TextField)
        MT5InputField(
            value = accountType,
            onValueChange = { accountType = it },
            label = "Bank Account Type"
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Account Number
            Box(modifier = Modifier.weight(1f)) {
                MT5InputField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = "Account Number"
                )
            }
            // Routing Number
            Box(modifier = Modifier.weight(1f)) {
                MT5InputField(
                    value = routingNumber,
                    onValueChange = { routingNumber = it },
                    label = "Routing Number"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Save Logic */ },
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