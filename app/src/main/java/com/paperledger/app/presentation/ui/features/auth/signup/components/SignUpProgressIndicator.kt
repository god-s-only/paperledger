package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.paperledger.app.presentation.theme.TradingBlue


@Composable
fun SignUpProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF1E1E1E)
    val activeColor = TradingBlue
    val inactiveColor = if (isDarkTheme) Color(0xFF424242) else Color(0xFFE0E0E0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalSteps) {
            ProgressStep(
                step = i + 1,
                isCompleted = i < currentStep,
                isActive = i == currentStep,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
            if (i < totalSteps - 1) {
                ProgressLine(
                    isCompleted = i < currentStep,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }
    }
}