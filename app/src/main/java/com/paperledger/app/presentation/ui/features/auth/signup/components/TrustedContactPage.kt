package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paperledger.app.domain.models.account.TrustedContactData


@Composable
fun TrustedContactPage(
    trustedContactData: TrustedContactData,
    onTrustedContactDataChange: (TrustedContactData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF37474F) else Color(
                    0xFFE3F2FD
                )
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Trusted Contact",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color(0xFF64B5F6) else Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please provide information for a trusted contact person. This person may be contacted if we cannot reach you.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "First Name",
                value = trustedContactData.givenName,
                onValueChange = { onTrustedContactDataChange(trustedContactData.copy(givenName = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "First name",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "Last Name",
                value = trustedContactData.familyName,
                onValueChange = { onTrustedContactDataChange(trustedContactData.copy(familyName = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "Last name",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }

        InputField(
            label = "Email Address",
            value = trustedContactData.emailAddress,
            onValueChange = { onTrustedContactDataChange(trustedContactData.copy(emailAddress = it)) },
            keyboardType = KeyboardType.Email,
            placeholder = "Contact email address",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
    }
}