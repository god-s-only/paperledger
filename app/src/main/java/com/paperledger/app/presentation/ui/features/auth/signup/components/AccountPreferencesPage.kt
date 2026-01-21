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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paperledger.app.domain.models.account.AccountPreferencesData

@Composable
fun AccountPreferencesPage(
    accountPreferences: AccountPreferencesData,
    onAccountPreferencesChange: (AccountPreferencesData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Funding Sources Section
        SelectionSection(
            title = "Funding Sources",
            description = "Select all that apply to your primary income source",
            options = listOf(
                "employment_income" to "Employment Income",
                "investment_income" to "Investment Income",
                "other" to "Other"
            ),
            selectedValues = accountPreferences.fundingSource,
            onSelectionChange = { selected ->
                onAccountPreferencesChange(
                    accountPreferences.copy(fundingSource = selected)
                )
            },
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )

        // Enabled Assets Section
        SelectionSection(
            title = "Enabled Trading Assets",
            description = "Select the assets you want to trade",
            options = listOf(
                "us_equity" to "US Equities",
                "us_option" to "US Options",
                "crypto" to "Cryptocurrency"
            ),
            selectedValues = accountPreferences.enabledAssets,
            onSelectionChange = { selected ->
                onAccountPreferencesChange(
                    accountPreferences.copy(enabledAssets = selected)
                )
            },
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
    }
}

@Composable
private fun SelectionSection(
    title: String,
    description: String,
    options: List<Pair<String, String>>,
    selectedValues: List<String>,
    onSelectionChange: (List<String>) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = if (isDarkTheme) Color.White else Color.Black
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = if (isDarkTheme) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { (key, label) ->
                OptionRow(
                    label = label,
                    isChecked = selectedValues.contains(key),
                    onCheckedChange = { isChecked ->
                        val newSelection = if (isChecked) {
                            selectedValues + key
                        } else {
                            selectedValues - key
                        }
                        onSelectionChange(newSelection)
                    },
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}

@Composable
private fun OptionRow(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isDarkTheme: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkTheme) Color.White else Color.Black
        )
    }
}