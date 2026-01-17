package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paperledger.app.domain.models.account.DisclosuresData


@Composable
fun DisclosuresPage(
    disclosuresData: DisclosuresData,
    onDisclosuresDataChange: (DisclosuresData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Please review and confirm the following disclosures:",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkTheme) Color.White else Color.Black
        )

        DisclosureItem(
            title = "Control Person",
            description = "I am a control person of a publicly traded company",
            isChecked = disclosuresData.isControlPerson,
            onCheckedChange = { onDisclosuresDataChange(disclosuresData.copy(isControlPerson = it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "FINRA Affiliate",
            description = "I am affiliated with a FINRA member firm",
            isChecked = disclosuresData.isAffiliatedExchangeOrFinra,
            onCheckedChange = {
                onDisclosuresDataChange(
                    disclosuresData.copy(
                        isAffiliatedExchangeOrFinra = it
                    )
                )
            },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Exchange/IIROC Affiliate",
            description = "I am affiliated with an exchange or IIROC member",
            isChecked = disclosuresData.isAffiliatedExchangeOrIiroc,
            onCheckedChange = {
                onDisclosuresDataChange(
                    disclosuresData.copy(
                        isAffiliatedExchangeOrIiroc = it
                    )
                )
            },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Politically Exposed",
            description = "I am a politically exposed person",
            isChecked = disclosuresData.isPoliticallyExposed,
            onCheckedChange = { onDisclosuresDataChange(disclosuresData.copy(isPoliticallyExposed = it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Family Exposed",
            description = "My immediate family is politically exposed",
            isChecked = disclosuresData.immediateFamilyExposed,
            onCheckedChange = { onDisclosuresDataChange(disclosuresData.copy(immediateFamilyExposed = it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )
    }
}