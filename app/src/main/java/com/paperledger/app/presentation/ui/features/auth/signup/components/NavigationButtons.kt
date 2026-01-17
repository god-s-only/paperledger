package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paperledger.app.presentation.theme.TradingBlue


@Composable
fun NavigationButtons(
    currentStep: Int,
    totalSteps: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onPreviousClick,
            enabled = currentStep > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkTheme) Color(0xFF424242) else Color(0xFFE0E0E0),
                disabledContainerColor = Color.Transparent,
                contentColor = if (isDarkTheme) Color.White else Color.Black
            ),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Previous",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Previous")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onNextClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = TradingBlue
            ),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Text(if (currentStep == totalSteps - 1) "Complete" else "Next")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                if (currentStep == totalSteps - 1) Icons.Default.CheckCircle else Icons.Default.ArrowForward,
                contentDescription = if (currentStep == totalSteps - 1) "Complete" else "Next",
                modifier = Modifier.size(18.dp)
            )
        }
    }
}