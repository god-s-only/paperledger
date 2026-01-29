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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.paperledger.app.core.UIEvent
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
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF1E1E1E)
    val borderColor = if (isDarkTheme) DarkBorder else LightBorder
    val surfaceColor = if (isDarkTheme) DarkSurface else LightSurface
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UIEvent.Navigate -> {
                    navController.navigate(event.route)
                }
                is UIEvent.PopBackStack -> {

                }
                is UIEvent.ShowSnackBar -> {

                }
            }
        }
    }

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Progress Indicator
                SignUpProgressIndicator(
                    currentStep = state.currentPage,
                    totalSteps = state.totalPages,
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
                        text = getPageTitle(state.currentPage),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = getPageDescription(state.currentPage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Error Message
                state.error?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = error,
                            color = Color(0xFFB71C1C),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // Page Content based on current page
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    when (state.currentPage) {
                        1 -> ContactInfoPage(
                            state = state,
                            onEvent = viewModel::onEvent,
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                        2 -> IdentityInfoPage(
                            state = state,
                            onEvent = viewModel::onEvent,
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                        3 -> DisclosuresPage(
                            state = state,
                            onEvent = viewModel::onEvent,
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                        4 -> DocumentsPage(
                            state = state,
                            onEvent = viewModel::onEvent,
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                        5 -> TrustedContactPage(
                            state = state,
                            onEvent = viewModel::onEvent,
                            surfaceColor = surfaceColor,
                            borderColor = borderColor,
                            isDarkTheme = isDarkTheme
                        )
                    }
                }

                // Navigation Buttons
                NavigationButtons(
                    currentStep = state.currentPage,
                    totalSteps = state.totalPages,
                    onPreviousClick = {
                        if (state.currentPage > 1) {
                            viewModel.onEvent(SignUpEvent.OnNavigateBack)
                        }
                    },
                    onNextClick = {
                        when (state.currentPage) {
                            1 -> viewModel.onEvent(SignUpEvent.OnNextFromContactPage)
                            2 -> viewModel.onEvent(SignUpEvent.OnNextFromIdentityPage)
                            3 -> viewModel.onEvent(SignUpEvent.OnNextFromDisclosuresPage)
                            4 -> viewModel.onEvent(SignUpEvent.OnNextFromDocumentsPage)
                            5 -> viewModel.onEvent(SignUpEvent.OnSubmitFromTrustedContactPage)
                        }
                    },
                    isDarkTheme = isDarkTheme,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Loading Overlay
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF2196F3),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Processing...",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

fun getPageTitle(step: Int): String = when (step) {
    1 -> "Contact Information"
    2 -> "Identity Information"
    3 -> "Disclosures"
    4 -> "Documents"
    5 -> "Trusted Contact"
    else -> ""
}

fun getPageDescription(step: Int): String = when (step) {
    1 -> "Let's start with your contact details"
    2 -> "Please provide your identity information"
    3 -> "Review and agree to the disclosures"
    4 -> "Upload required documents"
    5 -> "Add a trusted contact"
    else -> ""
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultPreview() {
    SignUpScreen(navController = rememberNavController())
}