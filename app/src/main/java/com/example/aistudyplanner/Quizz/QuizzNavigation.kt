package com.example.aistudyplanner.Quizz

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Composable
fun QuizzNavigation(
    backStack: NavBackStack,
    onBack: () -> Unit,
    application: Application,
    uriKey: String? = null,
) {

    var currentScreen by remember { mutableStateOf<QuizzRoutes>(QuizzRoutes.QmainScreen) }
    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    var isGeneratingQuiz by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val geminiViewModel = viewModel<GeminiViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GeminiViewModel(context) as T
        }
    }

    )


    val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    NavDisplay(
        backStack = backStack, onBack = {
            backStack.removeLastOrNull()
        }, modifier = Modifier.fillMaxSize(),

        entryProvider = entryProvider {
            firebaseCrashlytics.log("")
            entry<QuizzRoutes.QmainScreen> {
                firebaseCrashlytics.log("Currently inside of Quizz Screen")
                QuizzMainScreen(
                    onUploadPdf = { uri ->

                        firebaseCrashlytics.log("Uploading URI from recents to the pdf ")

                        selectedPdfUri = if (uriKey?.toUri() == null) uri else uriKey?.toUri()
                        if (uriKey?.toUri() == null)

                            geminiViewModel.setPDfquizz(uri)
                        else
                            geminiViewModel.setPDfquizz(uriKey.toUri())

                    }, isGenerating = isGeneratingQuiz, navBackState = backStack,
                    backButton = {
                        onBack.invoke()
                    },
                    application,
                    uriKey = uriKey

                )
            }


            entry<QuizzRoutes.QProcessingScreen> {
                firebaseCrashlytics.log("Currently inside of QProcessing Screen")
                ProcessingScreen(
                    pdfUri = selectedPdfUri,
                    newUri = uriKey?.toUri(),
                    navBackStack = backStack,

                    )
            }


            entry<QuizzRoutes.QuizzPannel> {
                firebaseCrashlytics.log("Currently insider of quizz pannel")
                QuizzPannel(navBackStackEntry = backStack, exitQuizz = {
                    onBack.invoke()
                })
//                }
            }

        }
    )
}
