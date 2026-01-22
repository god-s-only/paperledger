package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpEvent
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpState

@Composable
fun ContactInfoPage(
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        InputField(
            label = "Email Address",
            value = state.email,
            onValueChange = { onEvent(SignUpEvent.OnEmailChange(it)) },
            keyboardType = KeyboardType.Email,
            placeholder = "Enter your email",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Phone Number",
            value = state.phoneNumber,
            onValueChange = { onEvent(SignUpEvent.OnPhoneNumberChange(it)) },
            keyboardType = KeyboardType.Phone,
            placeholder = "Enter your phone number",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Street Address",
            value = state.streetAddress.firstOrNull() ?: "",
            onValueChange = { newAddress ->
                val updatedAddress = if (state.streetAddress.isEmpty()) {
                    listOf(newAddress, newAddress)
                } else {
                    state.streetAddress.mapIndexed { index, address ->
                        if (index == 0 || index == 1) newAddress else address
                    }
                }
                onEvent(SignUpEvent.OnStreetAddressChange(updatedAddress))
            },
            keyboardType = KeyboardType.Text,
            placeholder = "Enter your street address",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )

        InputField(
            label = "Unit / Apartment",
            value = state.unit,
            onValueChange = { onEvent(SignUpEvent.OnUnitChange(it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "Apt, Suite, Unit (optional)",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "City",
                value = state.city,
                onValueChange = { onEvent(SignUpEvent.OnCityChange(it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "City",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "State",
                value = state.state,
                onValueChange = { onEvent(SignUpEvent.OnStateChange(it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "State",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "Postal Code",
                value = state.postalCode,
                onValueChange = { onEvent(SignUpEvent.OnPostalCodeChange(it)) },
                keyboardType = KeyboardType.Number,
                placeholder = "Enter postal code",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }

        // Add padding at bottom to make last field visible above navigation buttons
        Spacer(modifier = Modifier.height(100.dp))
    }
}