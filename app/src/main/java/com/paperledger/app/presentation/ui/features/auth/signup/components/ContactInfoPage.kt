package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paperledger.app.domain.models.account.ContactData
import com.paperledger.app.presentation.ui.features.auth.signup.InputField

@Composable
fun ContactInfoPage(
    contactData: ContactData,
    onContactDataChange: (ContactData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InputField(
            label = "Email Address",
            value = contactData.emailAddress,
            onValueChange = { onContactDataChange(contactData.copy(emailAddress = it)) },
            keyboardType = KeyboardType.Email,
            placeholder = "Enter your email",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Phone Number",
            value = contactData.phoneNumber,
            onValueChange = { onContactDataChange(contactData.copy(phoneNumber = it)) },
            keyboardType = KeyboardType.Phone,
            placeholder = "Enter your phone number",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Street Address",
            value = contactData.streetAddress.firstOrNull() ?: "",
            onValueChange = { newAddress ->
                val updatedAddress = if (contactData.streetAddress.isEmpty()) {
                    listOf(newAddress, newAddress) // API expects both entries
                } else {
                    contactData.streetAddress.mapIndexed { index, address ->
                        if (index == 0 || index == 1) newAddress else address
                    }
                }
                onContactDataChange(contactData.copy(streetAddress = updatedAddress))
            },
            keyboardType = KeyboardType.Text,
            placeholder = "Enter your street address",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )

        InputField(
            label = "Unit / Apartment",
            value = contactData.unit,
            onValueChange = { onContactDataChange(contactData.copy(unit = it)) },
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
                value = contactData.city,
                onValueChange = { onContactDataChange(contactData.copy(city = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "City",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "State",
                value = contactData.state,
                onValueChange = { onContactDataChange(contactData.copy(state = it)) },
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
                value = contactData.postalCode,
                onValueChange = { onContactDataChange(contactData.copy(postalCode = it)) },
                keyboardType = KeyboardType.Number,
                placeholder = "Enter postal code",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
    }
}