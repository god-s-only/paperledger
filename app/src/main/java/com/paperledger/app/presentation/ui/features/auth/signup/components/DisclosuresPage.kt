package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpEvent
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpState


@Composable
fun DisclosuresPage(
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Text(
            text = "Please review and confirm the following disclosures:",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkTheme) Color.White else Color.Black
        )

        DisclosureItem(
            title = "Control Person",
            description = "I am a control person of a publicly traded company",
            isChecked = state.isControlPerson,
            onCheckedChange = { onEvent(SignUpEvent.OnIsControlPersonChange(it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Affiliated Exchange",
            description = "I am affiliated with an exchange",
            isChecked = state.isAffiliatedExchange,
            onCheckedChange = { onEvent(SignUpEvent.OnIsAffiliatedExchangeChange(it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Politically Exposed",
            description = "I am a politically exposed person",
            isChecked = state.isPoliticallyExposed,
            onCheckedChange = { onEvent(SignUpEvent.OnIsPoliticallyExposedChange(it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Family Exposed",
            description = "My immediate family is politically exposed",
            isChecked = state.immediateFamilyExposed,
            onCheckedChange = { onEvent(SignUpEvent.OnImmediateFamilyExposedChange(it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        // Add padding at bottom
        Spacer(modifier = Modifier.height(100.dp))
    }
}