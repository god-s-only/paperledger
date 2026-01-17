package com.paperledger.app.presentation.ui.features.auth.signup

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.graphics.BitmapFactory
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.paperledger.app.domain.models.account.ContactData
import com.paperledger.app.domain.models.account.DisclosuresData
import com.paperledger.app.domain.models.account.DocumentsData
import com.paperledger.app.domain.models.account.IdentityData
import com.paperledger.app.domain.models.account.SignUpData
import com.paperledger.app.domain.models.account.TrustedContactData
import com.paperledger.app.presentation.theme.DarkBorder
import com.paperledger.app.presentation.theme.DarkSurface
import com.paperledger.app.presentation.theme.LightBorder
import com.paperledger.app.presentation.theme.LightSurface
import com.paperledger.app.presentation.theme.TradingBlue
import com.paperledger.app.presentation.ui.features.auth.signup.components.ContactInfoPage
import com.paperledger.app.presentation.ui.features.auth.signup.components.DisclosuresPage
import com.paperledger.app.presentation.ui.features.auth.signup.components.DocumentsPage
import com.paperledger.app.presentation.ui.features.auth.signup.components.IdentityInfoPage
import com.paperledger.app.presentation.ui.features.auth.signup.components.InputField
import com.paperledger.app.presentation.ui.features.auth.signup.components.NavigationButtons
import com.paperledger.app.presentation.ui.features.auth.signup.components.SignUpProgressIndicator
import com.paperledger.app.presentation.ui.features.auth.signup.components.TrustedContactPage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Base64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUpComplete: (SignUpData) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 5 })
    val signUpData = remember { mutableStateOf(SignUpData()) }
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF1E1E1E)
    val borderColor = if (isDarkTheme) DarkBorder else LightBorder
    val surfaceColor = if (isDarkTheme) DarkSurface else LightSurface
    val coroutineScope = rememberCoroutineScope()

    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Create Account",
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Progress Indicator
            SignUpProgressIndicator(
                currentStep = currentStep,
                totalSteps = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Page Title
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = getPageTitle(currentStep),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color.Black
                )
                Text(
                    text = getPageDescription(currentStep),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // HorizontalPager for steps
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (page) {
                        0 -> ContactInfoPage(
                            contactData = signUpData.value.contact,
                            onContactDataChange = { signUpData.value = signUpData.value.copy(contact = it) },
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                        1 -> IdentityInfoPage(
                            identityData = signUpData.value.identity,
                            onIdentityDataChange = { signUpData.value = signUpData.value.copy(identity = it) },
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                        2 -> DisclosuresPage(
                            disclosuresData = signUpData.value.disclosures,
                            onDisclosuresDataChange = { signUpData.value = signUpData.value.copy(disclosures = it) },
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                        3 -> DocumentsPage(
                            documentsData = signUpData.value.documents,
                            onDocumentsDataChange = { signUpData.value = signUpData.value.copy(documents = it) },
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                        4 -> TrustedContactPage(
                            trustedContactData = signUpData.value.trustedContact,
                            onTrustedContactDataChange = { 
                                signUpData.value = signUpData.value.copy(trustedContact = it) 
                            },
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }

            // Navigation Buttons
            NavigationButtons(
                currentStep = currentStep,
                totalSteps = 5,
                onPreviousClick = {
                    if (currentStep > 0) {
                        currentStep--
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(currentStep)
                        }
                    }
                },
                onNextClick = {
                    if (currentStep < 4) {
                        currentStep++
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(currentStep)
                        }
                    } else {
                        onSignUpComplete(signUpData.value)
                    }
                },
                isDarkTheme = isDarkTheme,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}



fun getPageTitle(step: Int): String = when (step) {
    0 -> "Contact Information"
    1 -> "Identity Information"
    2 -> "Disclosures"
    3 -> "Documents"
    4 -> "Trusted Contact"
    else -> ""
}

fun getPageDescription(step: Int): String = when (step) {
    0 -> "Let's start with your contact details"
    1 -> "Please provide your identity information"
    2 -> "Review and agree to the disclosures"
    3 -> "Upload required documents"
    4 -> "Add a trusted contact"
    else -> ""
}

// Data classes


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultPreview() {
    SignUpScreen()
}