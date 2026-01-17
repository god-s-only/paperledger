package com.paperledger.app.presentation.ui.features.auth.signup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paperledger.app.presentation.theme.TradingBlue


@Composable
fun DropdownMenuField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    placeholder: String,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF1E1E1E)

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = if (isDarkTheme) Color.White else Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                placeholder = {
                    Text(
                        placeholder,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor,
                    disabledContainerColor = surfaceColor,
                    focusedIndicatorColor = TradingBlue,
                    unfocusedIndicatorColor = borderColor,
                    focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                    unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(surfaceColor)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}