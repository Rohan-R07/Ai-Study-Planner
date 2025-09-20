package com.example.aistudyplanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.unpackFloat1
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.Recents.DirectUriPdfSummarizerScreen
import com.example.aistudyplanner.Recents.RecentsDataStoreVM
import com.example.aistudyplanner.Utils.generatePdfPreview
import com.example.aistudyplanner.Utils.getPdfFileName
import com.example.aistudyplanner.pdfExtaction.PdfFilePicker
import com.example.aistudyplanner.ui.theme.AIStudyPlannerTheme
import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.example.aistudyplanner.ui.theme.CDotUnFocusedColour
import com.google.firebase.ai.type.content
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PdfSumScreen : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {

            val geminiViewModel = viewModels<GeminiViewModel>(
                factoryProducer = {

                    object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return GeminiViewModel(applicationContext) as T
                        }
                    }
                })

            val value = intent.getStringExtra("URI_KEY") // Replace with the appropriate type


            val isURIpresent = remember { mutableStateOf(value) }

            val context = LocalContext.current
            val summaryResonse = geminiViewModel.value.summary.collectAsState()


            var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
            var pdfName by remember { mutableStateOf("") }
            var pdfPreviewBitmap by remember { mutableStateOf<Bitmap?>(null) }

            var isLoading = geminiViewModel.value.isLoadingPdfSummary.collectAsState().value

            var showCopySuccess by remember { mutableStateOf(false) }

            val recentsvViewModel = viewModels<RecentsDataStoreVM>(
                factoryProducer = {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return RecentsDataStoreVM(application) as T
                        }
                    }
                }
            )


            val coroutineScope = rememberCoroutineScope()

            AIStudyPlannerTheme {
                if (isURIpresent.value == null) {

                    val pdfPickerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.OpenDocument(), onResult = { uri ->

                            if (uri != null) {
                                context.contentResolver.takePersistableUriPermission(
                                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                                )


                                recentsvViewModel.value.addRecentPdf(uri)
                                geminiViewModel.value.setPdfUri(uri)
                            }
                            uri?.let {
                                selectedPdfUri = it
                                pdfName = getPdfFileName(context, it)

                                coroutineScope.launch {
                                    pdfPreviewBitmap = generatePdfPreview(context, it)
                                }
                            }
                        }
                    )



                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        text = "Summarize PDF",
                                        color = White,
                                        fontSize = 20.sp
                                    )
                                },
                                navigationIcon = {
                                    IconButton(
                                        modifier = Modifier,
                                        onClick = {
                                            finish()
                                        }
                                    ) {
                                        Icon(

                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(10.dp)
                                        )
                                    }


                                }
                            )
                        },
                        contentColor = CBackground
                    ) { innerPadding ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {


                            item {
                                Column(
                                    modifier = Modifier
                                        .background(CBackground)
                                ) {


                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 24.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = CBackground,
                                        )

                                    ) {


                                        Column(
                                            modifier = Modifier
                                                .padding(20.dp)
                                                .background(CBackground),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {


                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "PDF Summarizer",
                                                modifier = Modifier.size(48.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = "PDF Summarizer",
                                                style = MaterialTheme.typography.headlineMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = "Select a PDF file to generate an AI summary",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center
                                            )

                                            // PDf Selection Button

                                            Button(
                                                onClick = {
                                                    pdfPickerLauncher.launch(arrayOf("application/pdf"))
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(56.dp),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "Select PDF",
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = if (selectedPdfUri != null) "Change PDF" else "Select PDF File",
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                            // TO check wether is there any uri form the home screen from recents
                                            Log.d("URi", isURIpresent.value.toString())


                                            selectedPdfUri?.let {


                                                Spacer(modifier = Modifier.height(24.dp))

                                                Card(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = 2.dp
                                                    ),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = CBackground
                                                    )
                                                ) {
                                                    Column(
                                                        modifier = Modifier.padding(16.dp)
                                                    ) {


                                                        // PDF name

                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Add,
                                                                contentDescription = "PDF Icon",
                                                                tint = Color(0xFFE53E3E),
                                                                modifier = Modifier.size(24.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(12.dp))
                                                            Column(modifier = Modifier.weight(1f)) {
                                                                Text(
                                                                    text = "Selected PDF:",
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                                )
                                                                Text(
                                                                    text = pdfName.ifEmpty { "Unknown PDF" },
                                                                    style = MaterialTheme.typography.bodyLarge,
                                                                    fontWeight = FontWeight.Medium,
                                                                    maxLines = 2,
                                                                    overflow = TextOverflow.Ellipsis
                                                                )
                                                            }
                                                        }


                                                        // PDF Preview

                                                        pdfPreviewBitmap?.let { bitmap ->
                                                            Spacer(modifier = Modifier.height(16.dp))
                                                            Text(
                                                                text = "Preview:",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                            Spacer(modifier = Modifier.height(8.dp))

                                                            Box(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(200.dp)
                                                                    .clip(RoundedCornerShape(8.dp))
                                                                    .background(
                                                                        Color.Gray.copy(
                                                                            alpha = 0.1f
                                                                        )
                                                                    ),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Image(
                                                                    bitmap = bitmap.asImageBitmap(),
                                                                    contentDescription = "PDF Preview",
                                                                    modifier = Modifier
                                                                        .fillMaxSize()
                                                                        .clip(RoundedCornerShape(8.dp)),
                                                                    contentScale = ContentScale.Fit
                                                                )
                                                            }

                                                            Spacer(modifier = Modifier.height(16.dp))

                                                            Button(
                                                                onClick = {
                                                                    geminiViewModel.value.extractAndSummarize()
                                                                },
                                                                modifier = Modifier.fillMaxWidth(),
                                                                enabled = !isLoading,
                                                                shape = RoundedCornerShape(8.dp)
                                                            ) {
                                                                if (isLoading) {
                                                                    CircularProgressIndicator(
                                                                        modifier = Modifier
                                                                            .size(20.dp),
                                                                        strokeWidth = 2.dp,
                                                                        color = CDotFocusedColor
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.width(
                                                                            12.dp
                                                                        )
                                                                    )
                                                                    Text("Generating Summary...")
                                                                } else {
                                                                    Icon(
                                                                        imageVector = Icons.Default.Add,
                                                                        contentDescription = "Summarize",
                                                                        modifier = Modifier.size(20.dp)
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.width(
                                                                            12.dp
                                                                        )
                                                                    )
                                                                    Text("Generate Summary")
                                                                }
                                                            }


                                                        }
                                                    }
                                                }

                                            }


                                        }

                                    }
                                }
                            }
                            item {


                                if (summaryResonse.value.toString()
                                        .isNotEmpty() && summaryResonse.value.toString() != "null"
                                ) {


                                    Spacer(modifier = Modifier.height(24.dp))

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                                alpha = 0.3f
                                            )
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "Summary",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = "AI Summary",
                                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = CDotFocusedColor
                                                )


                                                Spacer(Modifier.padding(horizontal = 70.dp))

                                                Button(
                                                    modifier = Modifier,
                                                    onClick = {
                                                        if (summaryResonse.value.toString()
                                                                .isNotEmpty()
                                                        ) {
                                                            val clipboardManager =
                                                                context.getSystemService(
                                                                    Context.CLIPBOARD_SERVICE
                                                                ) as ClipboardManager
                                                            val clip =
                                                                ClipData.newPlainText(
                                                                    "Summary",
                                                                    summaryResonse.value.toString()
                                                                )
                                                            clipboardManager.setPrimaryClip(clip)
                                                            showCopySuccess = true
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = CDotUnFocusedColour.copy(0.4f)
                                                    )
                                                ) {
                                                    Text(
                                                        if (showCopySuccess) "Copied!" else "Copy",
                                                        color = CDotFocusedColor
                                                    )
                                                }

                                                if (showCopySuccess) {
                                                    LaunchedEffect(showCopySuccess) {
                                                        delay(2000)
                                                        showCopySuccess = false
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(12.dp))

                                            Text(
                                                text = summaryResonse.value.toString(),
                                                color = White,
                                                fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                                lineHeight = 24.sp
                                            )
                                        }
                                    }

                                }
                            }

                        }

                    }

                }else{


                    DirectUriPdfSummarizerScreen(
                        pdfUri = isURIpresent.value!!.toUri(),
                        geminiViewModel = geminiViewModel,
                        onBackPressed = {
                            finish()
                        }
                    )
                }


            }
        }
    }
}


