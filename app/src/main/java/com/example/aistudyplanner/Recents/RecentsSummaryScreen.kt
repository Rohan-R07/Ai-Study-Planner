package com.example.aistudyplanner.Recents

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.R
import com.example.aistudyplanner.Utils.generatePdfPreview
import com.example.aistudyplanner.Utils.getPdfFileName
import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.example.aistudyplanner.ui.theme.CDotUnFocusedColour
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectUriPdfSummarizerScreen(
    pdfUri: Uri, geminiViewModel: Lazy<GeminiViewModel>, onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State variables
    var pdfName by remember { mutableStateOf("") }
    var pdfPreviewBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showCopySuccess by remember { mutableStateOf(false) }

    // Observe ViewModel states
    val isLoading by geminiViewModel.value.isLoadingPdfSummary.collectAsState()
    val summaryResponse by geminiViewModel.value.summary.collectAsState()

    // Initialize PDF data when screen loads
    LaunchedEffect(pdfUri) {
        // Set the PDF URI in the ViewModel
        geminiViewModel.value.setPdfUri(pdfUri)

        // Get PDF name
        pdfName = getPdfFileName(context, pdfUri)

        // Generate PDF preview
        pdfPreviewBitmap = generatePdfPreview(context, pdfUri)
    }

    geminiViewModel.value.setPdfUri(pdfUri)
    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "PDF Summary", color = White, fontSize = 20.sp
                )
            }, navigationIcon = {
                IconButton(
                    onClick = { onBackPressed() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(10.dp)
                    )
                }
            })
        }, contentColor = CBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CBackground)
        ) {

            // PDF Info and Preview Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = CBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "PDF Icon",
                                tint = Color(0xFFE53E3E),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "PDF Document",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = pdfName.ifEmpty { "Loading..." },
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // PDF Preview
                        pdfPreviewBitmap?.let { bitmap ->

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Document Preview",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(height = 12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Gray.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "PDF Preview",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))


                        Log.d("Response",summaryResponse.toString())


                        // Generate Summary Button
                        Button(
                            onClick = {
                                geminiViewModel.value.extractAndSummarize()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Generating Summary...",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Generate Summary",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (summaryResponse?.isNotEmpty() == true && summaryResponse != "null") "Regenerate Summary" else "Generate AI Summary",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Summary Results Section
            item {
                if (summaryResponse?.isNotEmpty() == true && summaryResponse != "null") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            // Summary Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "AI Summary",
                                        tint = CDotFocusedColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "AI Summary",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CDotFocusedColor
                                    )
                                }

                                // Copy Button
                                Button(
                                    onClick = {
                                        val clipboardManager = context.getSystemService(
                                            Context.CLIPBOARD_SERVICE
                                        ) as ClipboardManager
                                        val clip = ClipData.newPlainText("Summary", summaryResponse)
                                        clipboardManager.setPrimaryClip(clip)
                                        showCopySuccess = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CDotUnFocusedColour.copy(0.4f)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp, vertical = 8.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (showCopySuccess) Icons.Default.Check else Icons.Default.PlayArrow,
                                        contentDescription = "Copy",
                                        modifier = Modifier.size(18.dp),
                                        tint = CDotFocusedColor
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (showCopySuccess) "Copied!" else "Copy",
                                        color = CDotFocusedColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Summary Content
                            SelectionContainer {
                                Text(
                                    text = summaryResponse.toString(),
                                    color = White,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    lineHeight = 26.sp,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            // Copy success feedback
                            if (showCopySuccess) {
                                LaunchedEffect(showCopySuccess) {
                                    delay(2000)
                                    showCopySuccess = false
                                }
                            }
                        }
                    }
                }
            }

            // Loading state when generating summary
            if (isLoading && (summaryResponse?.isEmpty() == true || summaryResponse == "null")) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                strokeWidth = 4.dp,
                                color = CDotFocusedColor
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Analyzing your document...",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "This may take a few moments",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
