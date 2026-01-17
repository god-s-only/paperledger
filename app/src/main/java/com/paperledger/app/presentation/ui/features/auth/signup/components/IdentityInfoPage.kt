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
import com.paperledger.app.domain.models.account.IdentityData
import com.paperledger.app.presentation.ui.features.auth.signup.InputField


@Composable
fun IdentityInfoPage(
    identityData: IdentityData,
    onIdentityDataChange: (IdentityData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "First Name",
                value = identityData.givenName,
                onValueChange = { onIdentityDataChange(identityData.copy(givenName = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "First name",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "Last Name",
                value = identityData.familyName,
                onValueChange = { onIdentityDataChange(identityData.copy(familyName = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "Last name",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
        InputField(
            label = "Date of Birth",
            value = identityData.dateOfBirth,
            onValueChange = { onIdentityDataChange(identityData.copy(dateOfBirth = it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "YYYY-MM-DD",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "Country of Citizenship",
                value = identityData.countryOfCitizenship,
                onValueChange = { onIdentityDataChange(identityData.copy(countryOfCitizenship = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "e.g., USA",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "Country of Birth",
                value = identityData.countryOfBirth,
                onValueChange = { onIdentityDataChange(identityData.copy(countryOfBirth = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "e.g., USA",
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
                label = "Tax ID",
                value = identityData.taxId,
                onValueChange = { onIdentityDataChange(identityData.copy(taxId = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "Enter tax ID",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "Tax ID Type",
                value = identityData.taxIdType,
                onValueChange = { onIdentityDataChange(identityData.copy(taxIdType = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "e.g., USA_SSN",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
        InputField(
            label = "Country of Tax Residence",
            value = identityData.countryOfTaxResidence,
            onValueChange = { onIdentityDataChange(identityData.copy(countryOfTaxResidence = it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "e.g., USA",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Funding Source",
            value = identityData.fundingSource,
            onValueChange = { onIdentityDataChange(identityData.copy(fundingSource = it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "e.g., employment_income",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
    }
}