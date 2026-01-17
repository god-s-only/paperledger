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
import com.paperledger.app.presentation.theme.DarkBorder
import com.paperledger.app.presentation.theme.DarkSurface
import com.paperledger.app.presentation.theme.LightBorder
import com.paperledger.app.presentation.theme.LightSurface
import com.paperledger.app.presentation.theme.TradingBlue
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Base64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUpComplete: (SignUpData) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 5 })
    val signUpData = remember { mutableStateOf(SignUpData()) }
    val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF1E1E1E)
    val borderColor = if (isDarkTheme) DarkBorder else LightBorder
    val surfaceColor = if (isDarkTheme) DarkSurface else LightSurface
    val coroutineScope = rememberCoroutineScope()

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

@Composable
fun ContactInfoPage(
    contactData: ContactData,
    onContactDataChange: (ContactData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InputField(
            label = "Email Address",
            value = contactData.emailAddress,
            onValueChange = { onContactDataChange(contactData.copy(emailAddress = it)) },
            keyboardType = KeyboardType.Email,
            placeholder = "Enter your email",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Phone Number",
            value = contactData.phoneNumber,
            onValueChange = { onContactDataChange(contactData.copy(phoneNumber = it)) },
            keyboardType = KeyboardType.Phone,
            placeholder = "Enter your phone number",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Street Address",
            value = contactData.streetAddress,
            onValueChange = { onContactDataChange(contactData.copy(streetAddress = it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "Enter your street address",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "City",
                value = contactData.city,
                onValueChange = { onContactDataChange(contactData.copy(city = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "City",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "State",
                value = contactData.state,
                onValueChange = { onContactDataChange(contactData.copy(state = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "State",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
        InputField(
            label = "Postal Code",
            value = contactData.postalCode,
            onValueChange = { onContactDataChange(contactData.copy(postalCode = it)) },
            keyboardType = KeyboardType.Number,
            placeholder = "Enter postal code",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
    }
}

@Composable
fun IdentityInfoPage(
    identityData: IdentityData,
    onIdentityDataChange: (IdentityData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "First Name",
                value = identityData.givenName,
                onValueChange = { onIdentityDataChange(identityData.copy(givenName = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "First name",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "Last Name",
                value = identityData.familyName,
                onValueChange = { onIdentityDataChange(identityData.copy(familyName = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "Last name",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
        InputField(
            label = "Date of Birth",
            value = identityData.dateOfBirth,
            onValueChange = { onIdentityDataChange(identityData.copy(dateOfBirth = it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "YYYY-MM-DD",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "Country of Citizenship",
                value = identityData.countryOfCitizenship,
                onValueChange = { onIdentityDataChange(identityData.copy(countryOfCitizenship = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "e.g., USA",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "Country of Birth",
                value = identityData.countryOfBirth,
                onValueChange = { onIdentityDataChange(identityData.copy(countryOfBirth = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "e.g., USA",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputField(
                label = "Tax ID",
                value = identityData.taxId,
                onValueChange = { onIdentityDataChange(identityData.copy(taxId = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "Enter tax ID",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
            InputField(
                label = "Tax ID Type",
                value = identityData.taxIdType,
                onValueChange = { onIdentityDataChange(identityData.copy(taxIdType = it)) },
                keyboardType = KeyboardType.Text,
                placeholder = "e.g., USA_SSN",
                surfaceColor = surfaceColor,
                borderColor = borderColor,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.weight(1f)
            )
        }
        InputField(
            label = "Country of Tax Residence",
            value = identityData.countryOfTaxResidence,
            onValueChange = { onIdentityDataChange(identityData.copy(countryOfTaxResidence = it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "e.g., USA",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
        InputField(
            label = "Funding Source",
            value = identityData.fundingSource,
            onValueChange = { onIdentityDataChange(identityData.copy(fundingSource = it)) },
            keyboardType = KeyboardType.Text,
            placeholder = "e.g., employment_income",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )
    }
}

@Composable
fun DisclosuresPage(
    disclosuresData: DisclosuresData,
    onDisclosuresDataChange: (DisclosuresData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Please review and confirm the following disclosures:",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkTheme) Color.White else Color.Black
        )

        DisclosureItem(
            title = "Control Person",
            description = "I am a control person of a publicly traded company",
            isChecked = disclosuresData.isControlPerson,
            onCheckedChange = { onDisclosuresDataChange(disclosuresData.copy(isControlPerson = it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "FINRA Affiliate",
            description = "I am affiliated with a FINRA member firm",
            isChecked = disclosuresData.isAffiliatedExchangeOrFinra,
            onCheckedChange = {
                onDisclosuresDataChange(
                    disclosuresData.copy(
                        isAffiliatedExchangeOrFinra = it
                    )
                )
            },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Exchange/IIROC Affiliate",
            description = "I am affiliated with an exchange or IIROC member",
            isChecked = disclosuresData.isAffiliatedExchangeOrIiroc,
            onCheckedChange = {
                onDisclosuresDataChange(
                    disclosuresData.copy(
                        isAffiliatedExchangeOrIiroc = it
                    )
                )
            },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Politically Exposed",
            description = "I am a politically exposed person",
            isChecked = disclosuresData.isPoliticallyExposed,
            onCheckedChange = { onDisclosuresDataChange(disclosuresData.copy(isPoliticallyExposed = it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )

        DisclosureItem(
            title = "Family Exposed",
            description = "My immediate family is politically exposed",
            isChecked = disclosuresData.immediateFamilyExposed,
            onCheckedChange = { onDisclosuresDataChange(disclosuresData.copy(immediateFamilyExposed = it)) },
            surfaceColor = surfaceColor,
            borderColor = borderColor
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DocumentsPage(
    documentsData: DocumentsData,
    onDocumentsDataChange: (DocumentsData) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    val context = LocalContext.current

    // For preview: this will not work, so fallback to plain UI in preview
    val isPreview = LocalContext.current.applicationContext.toString().contains("Preview")
    var isLoading by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }

    // File picker using activity result API (for images/pdfs, etc.)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            isLoading = true
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.let {
                val byteArray = it.readBytes()
                it.close()
                // Encode to Base64
                val base64Content = Base64.getEncoder().encodeToString(byteArray)
                // Optionally, try to fetch the file name
                fileName = uri.lastPathSegment ?: "Document"
                onDocumentsDataChange(documentsData.copy(content = base64Content))
            }
            isLoading = false
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = TradingBlue,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Upload Identity Document",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color.Black
                )
                Text(
                    text = "Please upload a valid government-issued ID (e.g. photo, PDF, etc.)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        DropdownMenuField(
            label = "Document Type",
            value = documentsData.documentType,
            onValueChange = { onDocumentsDataChange(documentsData.copy(documentType = it)) },
            options = listOf(
                "identity_verification",
                "passport",
                "drivers_license",
                "national_id"
            ),
            placeholder = "Select document type",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )

        DropdownMenuField(
            label = "Document Sub-type",
            value = documentsData.documentSubType,
            onValueChange = { onDocumentsDataChange(documentsData.copy(documentSubType = it)) },
            options = listOf(
                "passport",
                "license"
            ),
            placeholder = "Select sub-type",
            surfaceColor = surfaceColor,
            borderColor = borderColor,
            isDarkTheme = isDarkTheme
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Upload section: if content exists, show File info and delete button, otherwise show upload button
        if (documentsData.content.isEmpty()) {
            Button(
                onClick = { launcher.launch("*/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TradingBlue
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Upload",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Choose File")
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TradingBlue, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "File attached",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TradingBlue
                        )
                        Text(
                            text = if (fileName.isNotEmpty()) fileName else "Selected file",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        // Optionally show a preview if it's an image (very basic, safe/side-effect-free in composable)
                        val previewBitmap: ImageBitmap? = remember(documentsData.content) {
                            runCatching {
                                val data = Base64.getDecoder().decode(documentsData.content)
                                android.graphics.BitmapFactory.decodeByteArray(data, 0, data.size)
                                    ?.asImageBitmap()
                            }.getOrNull()
                        }

                        if (previewBitmap != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                bitmap = previewBitmap,
                                contentDescription = "Preview",
                                modifier = Modifier
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            onDocumentsDataChange(
                                documentsData.copy(content = "")
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove file",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

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

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    placeholder: String,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = if (isDarkTheme) Color.White else Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth(),
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
    }
}

@Composable
fun DisclosureItem(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    surfaceColor: Color,
    borderColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        shape = RoundedCornerShape(8.dp),
        border = if (isChecked)
            androidx.compose.foundation.BorderStroke(1.dp, TradingBlue)
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (MaterialTheme.colorScheme.background == Color(0xFF1E1E1E)) Color.White else Color.Black
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            // Sleeker switch design using new Material3 Switch and accent blue
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                thumbContent = {
                    if (isChecked) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checked",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TradingBlue,
                    checkedTrackColor = TradingBlue.copy(alpha = 0.3f),
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                ),
                modifier = Modifier.height(32.dp)
            )
        }
    }
}

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
data class SignUpData(
    val contact: ContactData = ContactData(),
    val identity: IdentityData = IdentityData(),
    val disclosures: DisclosuresData = DisclosuresData(),
    val documents: DocumentsData = DocumentsData(),
    val trustedContact: TrustedContactData = TrustedContactData()
)

data class ContactData(
    val emailAddress: String = "",
    val phoneNumber: String = "",
    val streetAddress: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = ""
)

data class IdentityData(
    val givenName: String = "",
    val familyName: String = "",
    val dateOfBirth: String = "",
    val countryOfCitizenship: String = "",
    val countryOfBirth: String = "",
    val partyType: String = "",
    val taxId: String = "",
    val taxIdType: String = "",
    val countryOfTaxResidence: String = "",
    val fundingSource: String = ""
)

data class DisclosuresData(
    val isControlPerson: Boolean = false,
    val isAffiliatedExchangeOrFinra: Boolean = false,
    val isAffiliatedExchangeOrIiroc: Boolean = false,
    val isPoliticallyExposed: Boolean = false,
    val immediateFamilyExposed: Boolean = false
)

data class DocumentsData(
    val documentType: String = "",
    val documentSubType: String = "",
    val content: String = ""
)

data class TrustedContactData(
    val givenName: String = "",
    val familyName: String = "",
    val emailAddress: String = ""
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultPreview() {
    SignUpScreen()
}