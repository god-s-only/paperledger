package com.paperledger.app.presentation.ui.features.auth.signup.components

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paperledger.app.domain.models.account.DocumentsData
import com.paperledger.app.presentation.theme.TradingBlue
import com.paperledger.app.presentation.ui.features.auth.signup.DropdownMenuField
import java.util.Base64

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