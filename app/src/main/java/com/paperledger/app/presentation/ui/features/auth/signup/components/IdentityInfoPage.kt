package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paperledger.app.domain.models.account.IdentityData
import com.paperledger.app.presentation.ui.features.auth.signup.InputField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text


@Composable
fun IdentityInfoPage(
    identityData: IdentityData,
    onIdentityDataChange: (IdentityData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean,
    availableFundingSources: List<String> = listOf(
        "employment_income",
        "savings",
        "inheritance",
        "business_income",
        "investment_income"
    ),
    availableAssets: List<String> = listOf(
        "stocks",
        "bonds",
        "real_estate",
        "crypto",
        "cash"
    )
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
        Text(
            text = "Funding Source(s)",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkTheme) Color.White else Color.Black
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            availableFundingSources.forEach { source ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = identityData.selectedFundingSources?.contains(source) == true,
                        onCheckedChange = { checked ->
                            val currentSources = identityData.selectedFundingSources ?: emptyList()
                            val newSources = if (checked) {
                                currentSources + source
                            } else {
                                currentSources - source
                            }
                            onIdentityDataChange(identityData.copy(selectedFundingSources = newSources))
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = source.replace('_', ' ').replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enabled Assets",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkTheme) Color.White else Color.Black
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            availableAssets.forEach { asset ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = identityData.selectedAssets?.contains(asset) == true,
                        onCheckedChange = { checked ->
                            val currentAssets = identityData.selectedAssets ?: emptyList()
                            val newAssets = if (checked) {
                                currentAssets + asset
                            } else {
                                currentAssets - asset
                            }
                            onIdentityDataChange(identityData.copy(selectedAssets = newAssets))
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = asset.replace('_', ' ').replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}