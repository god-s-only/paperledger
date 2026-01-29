package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpEvent
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpState

@Composable
fun IdentityInfoPage(
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
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
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "First Name",
                value = state.firstName,
                onValueChange = { onEvent(SignUpEvent.OnFirstNameChange(it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "First name",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "Last Name",
                value = state.lastName,
                onValueChange = { onEvent(SignUpEvent.OnLastNameChange(it)) },
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
            value = state.dateOfBirth,
            onValueChange = { onEvent(SignUpEvent.OnDateOfBirthChange(it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "YYYY-MM-DD",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Country Code",
            value = state.countryCode,
            onValueChange = { onEvent(SignUpEvent.OnCountryCodeChange(it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "e.g., US",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Tax ID",
            value = state.taxId,
            onValueChange = { onEvent(SignUpEvent.OnTaxIdChange(it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "Enter tax ID",
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
                        checked = state.fundingSource.contains(source),
                        onCheckedChange = { checked ->
                            val currentSources = state.fundingSource
                            val newSources = if (checked) {
                                currentSources + source
                            } else {
                                currentSources - source
                            }
                            onEvent(SignUpEvent.OnFundingSourcesChange(newSources))
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
                        checked = state.enabledAssets.contains(asset),
                        onCheckedChange = { checked ->
                            val currentAssets = state.enabledAssets
                            val newAssets = if (checked) {
                                currentAssets + asset
                            } else {
                                currentAssets - asset
                            }
                            onEvent(SignUpEvent.OnEnabledAssetsChange(newAssets))
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

        // Add padding at bottom
        Spacer(modifier = Modifier.height(100.dp))
    }
}