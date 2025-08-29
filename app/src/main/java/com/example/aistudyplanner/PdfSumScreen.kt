package com.example.aistudyplanner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.unpackFloat1
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.pdfExtaction.PdfFilePicker
import com.example.aistudyplanner.ui.theme.AIStudyPlannerTheme
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper

class PdfSumScreen : ComponentActivity() {

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
                }
            )

            AIStudyPlannerTheme {

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {

                        item {

                            val context = LocalContext.current
                            val selectedPdfUri by geminiViewModel.value.selectedPdfUri.collectAsState()
                            val extractedText by geminiViewModel.value.extractedText.collectAsState()

                            val summary by geminiViewModel.value.summary.collectAsState()

                            val pdfPickerLauncher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.OpenDocument(),
                                onResult = { uri ->
                                    if (uri != null) {
                                        context.contentResolver.takePersistableUriPermission(
                                            uri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        )

                                        geminiViewModel.value.setPdfUri(uri)
                                    }
                                }
                            )


                            Button(
                                onClick = {
                                    pdfPickerLauncher.launch(arrayOf("application/pdf"))
                                }
                            ) {
                                Text("Select PDF")
                            }


                            selectedPdfUri?.let {
                                Text("Selected: $it")
                                Spacer(Modifier.height(8.dp))
                                Button(onClick = { geminiViewModel.value.extractPdfText() }) {
                                    Text("Extract Text")
                                }
                            }

//                            extractedText?.let {
//                                Text("Extracted: ${it}...")
//                                Spacer(Modifier.height(8.dp))
//                                Button(onClick = { geminiViewModel.value.() }) {
//                                    Text("Summarize PDF")
//                                }
//                            }

                            summary?.let {
                                Text("Summary: $it")
                            }
                        }
                    }
                }
            }
        }
    }

}
