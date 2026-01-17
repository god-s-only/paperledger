package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressLine(
    isCompleted: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    val lineAlpha by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300),
        label = "lineAlpha"
    )

    Box(
        modifier = modifier
            .height(4.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(
                if (isCompleted) activeColor else inactiveColor,
                RoundedCornerShape(2.dp)
            )
            .alpha(lineAlpha)
    )
}