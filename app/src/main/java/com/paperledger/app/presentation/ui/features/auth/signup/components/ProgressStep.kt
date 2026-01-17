package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun ProgressStep(
    step: Int,
    isCompleted: Boolean,
    isActive: Boolean,
    activeColor: Color,
    inactiveColor: Color
) {
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF1E1E1E)
    val backgroundColor = when {
        isCompleted -> activeColor
        isActive -> activeColor
        else -> inactiveColor
    }
    val textColor = when {
        isCompleted -> Color.White
        isActive -> Color.White
        else -> if (isDarkTheme) Color.White else Color.Black
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                backgroundColor,
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = step.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}