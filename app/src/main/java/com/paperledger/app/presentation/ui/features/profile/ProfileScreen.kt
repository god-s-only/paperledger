package com.paperledger.app.presentation.ui.features.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.paperledger.app.core.UIEvent
import com.paperledger.app.domain.models.profile.ProfileModel
import com.paperledger.app.presentation.ui.features.trade.MT5_BLUE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.PopBackStack -> navController.popBackStack()
                is UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(
                    message = event.message,
                    duration = SnackbarDuration.Long
                )
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "PROFILE",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MT5_BLUE
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadProfile() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MT5_BLUE
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = Color(0xFFB71C1C),
                    contentColor = Color.White
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MT5_BLUE
                    )
                }

                state.error != null && state.profile == null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = state.error ?: "Something went wrong",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                        Button(
                            onClick = { viewModel.loadProfile() },
                            colors = ButtonDefaults.buttonColors(containerColor = MT5_BLUE)
                        ) {
                            Text("RETRY")
                        }
                    }
                }

                state.profile != null -> {
                    ProfileContent(profile = state.profile!!)
                }
            }
        }
    }
}

@Composable
fun ProfileContent(profile: ProfileModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Account header card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MT5_BLUE)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "${profile.givenName} ${profile.familyName}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = profile.emailAddress,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusBadge(label = profile.status)
                    StatusBadge(label = profile.currency)
                    StatusBadge(label = profile.accountType.replace("_", " ").uppercase())
                }
            }
        }

        ProfileSection(title = "ACCOUNT") {
            ProfileRow(icon = Icons.Default.Tag, label = "Account Number", value = profile.accountNumber)
            ProfileRow(icon = Icons.Default.CalendarToday, label = "Member Since", value = profile.createdAt)
            ProfileRow(icon = Icons.Default.AccountBalanceWallet, label = "Last Equity", value = "$${profile.lastEquity}")
        }

        ProfileSection(title = "IDENTITY") {
            ProfileRow(icon = Icons.Default.Person, label = "Given Name", value = profile.givenName)
            ProfileRow(icon = Icons.Default.Person, label = "Family Name", value = profile.familyName)
            ProfileRow(icon = Icons.Default.Cake, label = "Date of Birth", value = profile.dateOfBirth)
            ProfileRow(icon = Icons.Default.Public, label = "Country of Birth", value = profile.countryOfBirth)
            ProfileRow(icon = Icons.Default.Public, label = "Citizenship", value = profile.countryOfCitizenship)
            ProfileRow(icon = Icons.Default.Badge, label = "Tax ID Type", value = profile.taxIdType)
            ProfileRow(
                icon = Icons.Default.Payments,
                label = "Funding Source",
                value = profile.fundingSource.joinToString(", ") { it.replace("_", " ") }
            )
        }

        ProfileSection(title = "CONTACT") {
            ProfileRow(icon = Icons.Default.Email, label = "Email", value = profile.emailAddress)
            ProfileRow(icon = Icons.Default.Phone, label = "Phone", value = profile.phoneNumber)
            ProfileRow(
                icon = Icons.Default.Home,
                label = "Address",
                value = buildString {
                    append(profile.streetAddress.joinToString(", "))
                    if (profile.city.isNotBlank()) append(", ${profile.city}")
                    if (profile.state.isNotBlank()) append(", ${profile.state}")
                    if (profile.postalCode.isNotBlank()) append(" ${profile.postalCode}")
                }
            )
        }

        ProfileSection(title = "TRUSTED CONTACT") {
            ProfileRow(
                icon = Icons.Default.People,
                label = "Name",
                value = "${profile.trustedContactGivenName} ${profile.trustedContactFamilyName}".trim()
                    .ifBlank { "Not provided" }
            )
            ProfileRow(
                icon = Icons.Default.Email,
                label = "Email",
                value = profile.trustedContactEmail.ifBlank { "Not provided" }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MT5_BLUE,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                content()
            }
        }
    }
}

@Composable
fun ProfileRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MT5_BLUE,
            modifier = Modifier
                .size(18.dp)
                .offset(y = 2.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    )
}

@Composable
fun StatusBadge(label: String) {
    Surface(
        color = Color.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}