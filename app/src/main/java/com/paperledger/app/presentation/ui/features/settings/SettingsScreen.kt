package com.paperledger.app.presentation.ui.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.paperledger.app.presentation.ui.features.trade.MT5_BLUE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "SETTINGS",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item { SettingsSectionHeader("ACCOUNT") }
            item {
                SettingsItem(
                    title = "Profile Details",
                    subtitle = "Manage your personal information",
                    icon = Icons.Default.Person,
                    onClick = { /* Navigate to Profile */ }
                )
            }
            item {
                SettingsItem(
                    title = "Funding & Bank Accounts",
                    subtitle = "ACH relationships and deposits",
                    icon = Icons.Default.AccountBalance,
                    onClick = { /* Navigate to Funding */ }
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp) }

            item { SettingsSectionHeader("PREFERENCES") }
            item {
                SettingsSwitchItem(
                    title = "Dark Mode",
                    subtitle = "Toggle app visual theme",
                    icon = Icons.Default.DarkMode,
                    checked = true, // Hook up to your theme data store
                    onCheckedChange = { /* Update Theme */ }
                )
            }
            item {
                SettingsItem(
                    title = "Notifications",
                    subtitle = "Alerts for fills and price action",
                    icon = Icons.Default.Notifications,
                    onClick = { /* Navigate to Notifications */ }
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp) }

            item { SettingsSectionHeader("SECURITY & LEGAL") }
            item {
                SettingsItem(
                    title = "Security",
                    subtitle = "Passcode and Biometrics",
                    icon = Icons.Default.Security,
                    onClick = { /* Navigate to Security */ }
                )
            }
            item {
                SettingsItem(
                    title = "Legal & Privacy",
                    icon = Icons.Default.Description,
                    onClick = { /* Open WebView with Legal docs */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { /* Logout Logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("LOG OUT", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        color = MT5_BLUE,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = { Icon(icon, contentDescription = null, tint = MT5_BLUE) },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray) },
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, contentDescription = null, tint = MT5_BLUE) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = MT5_BLUE)
            )
        }
    )
}