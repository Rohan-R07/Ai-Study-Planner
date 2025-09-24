package com.example.aistudyplanner.Quizz

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProcessingScreen(
    pdfUri: Uri?,
    newUri: Uri?,
    navBackStack: NavBackStack
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->

        // TODo it is seacrhing with the pdfUrl from the Epandable Menu but i am using that Home recents menu

        val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

        val context = LocalContext.current

        val geminiViewModel = viewModel<GeminiViewModel>(
            factory =
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return GeminiViewModel(context) as T
                    }
                }
        )

        var processingStep by remember { mutableStateOf(0) }
        val processingSteps = listOf(
            "Reading PDF content...",
            "Analyzing document structure...",
            "Extracting Context",
            "Making the prompt",
            "Finishing.."
        )


        val extractedTextFromPdfSucessfull =
            geminiViewModel.sucessfulyCreatedQuizz.collectAsState().value
//
        Log.d("ExtractedTexting", extractedTextFromPdfSucessfull.toString())

        val quizz = geminiViewModel.quizState.collectAsState()

        Log.d(
            "ExtractedTextingRohan",
            geminiViewModel.sucessfulyCreatedQuizz.collectAsState().value.toString()
        )

        Log.d("RecentsQuizx", quizz.value?.title.toString())

        if (quizz.value?.title.toString() != "null") {


            navBackStack.add(QuizzRoutes.QuizzPannel)
            navBackStack.remove(QuizzRoutes.QProcessingScreen)
            Log.d("ExtractedTexting", extractedTextFromPdfSucessfull.toString())

        }

        Log.d("pdfURL", pdfUri.toString())

        val context1 = LocalContext.current

        Log.d("ExtractedTexting", extractedTextFromPdfSucessfull.toString())
        firebaseCrashlytics.log("Checkoing wether new uri is null or not ")
        if (newUri == null) {
            firebaseCrashlytics.log("new uril us null ")
            LaunchedEffect(pdfUri) {
                if (pdfUri != null) {
                    firebaseCrashlytics.log("pdf uri is not null ")
                    geminiViewModel.setPDfquizz(pdfUri)
                    geminiViewModel.generateQuizz()


                    // Simulate processing steps
                    for (i in processingSteps.indices) {
                        processingStep = i
                        delay(1000) // Simulate processing time
                    }
                } else {
                    firebaseCrashlytics.log("pdf uri is null")

                    Toast.makeText(context1, "Error in reading the pdf", Toast.LENGTH_LONG).show()
                }

                Log.d("ExtractedTexting", extractedTextFromPdfSucessfull.toString())
            }


        } else {
            LaunchedEffect(newUri) {

                firebaseCrashlytics.log("new uri is not null")


                geminiViewModel.setPDfquizz(newUri)
                geminiViewModel.generateQuizz()

                // Simulate processing steps
                for (i in processingSteps.indices) {
                    processingStep = i
                    delay(1000) // Simulate processing time
                }
                Log.d("ExtractedTexting", extractedTextFromPdfSucessfull.toString())
            }
        }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Processing Animation
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        ContainedLoadingIndicator(
                            containerColor = Unspecified,
                            indicatorColor = CDotFocusedColor,
                            polygons = listOf(
                                MaterialShapes.Pill,
                                MaterialShapes.Oval,
                                MaterialShapes.Boom,
                                MaterialShapes.Bun,
                            ),
                            modifier = Modifier
                                .size(100.dp)
                        )


                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Generating Your Quiz",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (processingStep < processingSteps.size) processingSteps[processingStep] else "Almost done...",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    LinearProgressIndicator(
                        progress = (processingStep + 1).toFloat() / processingSteps.size,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF667eea)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = {
                    firebaseCrashlytics.log("removing QProcesing screen")

                    navBackStack.remove(QuizzRoutes.QProcessingScreen)
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("Cancel")
            }
        }
    }


}
