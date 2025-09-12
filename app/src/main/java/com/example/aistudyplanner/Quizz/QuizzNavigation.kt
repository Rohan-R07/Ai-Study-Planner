package com.example.aistudyplanner.Quizz

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.aistudyplanner.Gemini.GeminiViewModel

@Composable
fun QuizzNavigation(backStack: NavBackStack) {

    var currentScreen by remember { mutableStateOf<QuizzRoutes>(QuizzRoutes.QmainScreen) }
    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    var currentQuiz by remember { mutableStateOf<Quiz?>(null) }
    var isGeneratingQuiz by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val geminiViewModel = viewModel<GeminiViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GeminiViewModel(context) as T
        }
    }

    )
    NavDisplay(
        backStack = backStack, onBack = {
            backStack.removeLastOrNull()
        }, modifier = Modifier.fillMaxSize(),

        entryProvider = entryProvider {

            entry<QuizzRoutes.QmainScreen> {
                QuizzMainScreen(
                    onUploadPdf = { uri ->

                        selectedPdfUri = uri
                        geminiViewModel.setPDfquizz(uri)
                        currentScreen = QuizzRoutes.QProcessingScreen
                    }, isGenerating = isGeneratingQuiz, navBackState = backStack

                )
            }


            entry<QuizzRoutes.QProcessingScreen> {
                ProcessingScreen(
                    pdfUri = selectedPdfUri, onQuizGenerated = { quiz ->
                        currentScreen = QuizzRoutes.QuizzPannel
                        isGeneratingQuiz = false
                    }, backStack

                )
            }


            entry<QuizzRoutes.QuizzPannel> {
//                currentQuiz?.let { quiz ->
                QuizzPannel(navBackStackEntry = backStack)
//                }
            }

        }
    )
}
