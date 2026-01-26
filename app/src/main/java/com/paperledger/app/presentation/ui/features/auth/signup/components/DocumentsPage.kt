package com.paperledger.app.presentation.ui.features.auth.signup.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.paperledger.app.presentation.theme.TradingBlue
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpEvent
import com.paperledger.app.presentation.ui.features.auth.signup.SignUpState
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DocumentsPage(
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    surfaceColor: Color,
    borderColor: Color,
    isDarkTheme: Boolean
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Create a temporary file for camera capture
    val photoFile = remember {
        File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg").apply {
            createNewFile()
        }
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // Function to convert image to Base64
    fun convertImageToBase64(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Compress bitmap to JPEG format
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            val byteArray = outputStream.toByteArray()

            // Encode to Base64
            Base64.getEncoder().encodeToString(byteArray)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Gallery picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            isLoading = true
            val base64Content = convertImageToBase64(uri)
            if (base64Content != null) {
                onEvent(SignUpEvent.OnDocumentUploaded(base64Content))
                fileName = uri.lastPathSegment ?: "Gallery Image"
            }
            isLoading = false
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            isLoading = true
            val base64Content = convertImageToBase64(photoUri)
            if (base64Content != null) {
                onEvent(SignUpEvent.OnDocumentUploaded(base64Content))
                fileName = "Camera Capture"
                capturedImageUri = photoUri
            }
            isLoading = false
        }
    }

    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.verticalScroll(scrollState)
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
                    text = "Please upload a valid government-issued ID (take a photo or select from gallery)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (state.uploadedDocuments.isEmpty()) {
            // Camera Button
            Button(
                onClick = { cameraLauncher.launch(photoUri) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TradingBlue
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Take Photo",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Take Photo")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Gallery Button
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TradingBlue.copy(alpha = 0.8f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Choose from Gallery",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Choose from Gallery")
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
                            text = "${state.uploadedDocuments.size} file(s) attached",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TradingBlue
                        )
                        Text(
                            text = if (fileName.isNotEmpty()) fileName else "Selected file",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                    IconButton(
                        onClick = {
                            state.uploadedDocuments.firstOrNull()?.let { docId ->
                                onEvent(SignUpEvent.OnDocumentRemoved(docId))
                                fileName = ""
                                capturedImageUri = null
                            }
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

        Spacer(modifier = Modifier.height(100.dp))
    }
}