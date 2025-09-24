package com.example.aistudyplanner.Quizz

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.BottomNavigation.BottomNavBarItems
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.Recents.QuizMainScreenWithUri
import com.example.aistudyplanner.Recents.RecentsDataStoreVM
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.bouncycastle.crypto.params.Blake3Parameters.context

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizzMainScreen(
    onUploadPdf: (Uri) -> Unit,
    isGenerating: Boolean,
    navBackState: NavBackStack,
    backButton: () -> Unit,
    application: android.app.Application,
    uriKey: String? = null
) {

    val firebaseCrashlyics = FirebaseCrashlytics.getInstance()

    firebaseCrashlyics.log("QMainScreen")

    val recentsvViewModel = viewModel<RecentsDataStoreVM>(
        factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RecentsDataStoreVM(application) as T
                }

            }
    )

    val context = LocalContext.current

    val geminiViewModel = viewModel<GeminiViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GeminiViewModel(context) as T
            }
        }
    )


    val isURIpresent = remember { mutableStateOf(uriKey) }


    if (isURIpresent.value == null) {


        val isPDFSelected = remember { mutableStateOf<Boolean?>(null) }
        val pdfLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            firebaseCrashlyics.log("lauching pdf selector")

            uri?.let { onUploadPdf(it) }
            val se = uri != null
            Log.d("SucessfullPdf", se.toString())

                firebaseCrashlyics.log("checking wether PDF uri is null or not")
            if (uri != null) {

                firebaseCrashlyics.log("uri is not null")
                isPDFSelected.value = true

                recentsvViewModel.addRecentPdf(uri)
            } else {
                firebaseCrashlyics.log("uri is null")
                isPDFSelected.value = false
            }
        }

        val context = LocalContext.current


        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {

                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                backButton.invoke()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp),

                                )
                        }
                    }
                )
            }

        ) { innerPadding ->


            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                // App Title
                Text(
                    text = "AI Quiz Generator",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))



                Text(
                    text = "Upload PDF to generate intelligent quizzes",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                val context = LocalContext.current


                Spacer(modifier = Modifier.height(48.dp))

                // Upload Button
                AnimatedVisibility(
                    visible = !isGenerating,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                firebaseCrashlyics.log("Lauching PDF selector")
                                pdfLauncher.launch("application/pdf")
                            },
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {

                        Log.d("SucessfullPdf", isPDFSelected.value.toString())

                            firebaseCrashlyics.log("checking if pdf selector is true or false")
                        if (isPDFSelected.value == true) {

                            firebaseCrashlyics.log("pdf selector is true ")
                            navBackState.add(QuizzRoutes.QProcessingScreen)
                            Log.d("SucessfullPdf", "working bro")
                        } else if (isPDFSelected.value == false) {

                            firebaseCrashlyics.log("pdf selector is false")
                            Log.d("SucessfullPdf", "Not working at al borther")
                        }


                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Upload PDF",
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF667eea)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Upload PDF",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF667eea)
                            )

                            Text(
                                text = "Tap to select",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }




                Spacer(modifier = Modifier.height(32.dp))

                // Features List
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Features",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        FeatureItem("📄", "PDF Analysis", "Extract content from any PDF document")
                        FeatureItem("🤖", "AI Generated", "Smart questions based on content")
                        FeatureItem("📊", "Multiple Choice", "Well-structured MCQ format")
                        FeatureItem("⚡", "Instant Results", "Get your quiz in seconds")
                    }
                }


            }


        }
    } else {

        QuizMainScreenWithUri(
            pdfUri = uriKey?.toUri()!!,
            navBackState,
            backButton,
            onNavigateToProcessing = {
                firebaseCrashlyics.log("Navigating to Processing")
                geminiViewModel.generateQuizz()
            }
        )


    }


}

@Composable
fun FeatureItem(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            modifier = Modifier.padding(end = 16.dp)
        )

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}