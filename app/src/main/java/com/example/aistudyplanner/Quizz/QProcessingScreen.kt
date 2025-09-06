package com.example.aistudyplanner.Quizz

import android.net.Uri
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import kotlinx.coroutines.delay

val json =
    """
    [
        {
            "id": 1,
            "name": "John Doe",
            "title": "Software Engineer",
            "age": 28
        },
        {
            "id": 2,
            "name": "Jane Smith",
            "title": "Product Manager",
            "age": 32
        },
        {
            "id": 3,
            "name": "Alex Johnson",
            "title": "UI/UX Designer",
            "age": 26
        }
    ]
    """
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProcessingScreen(
    pdfUri: Uri?,
    onQuizGenerated: (Quiz) -> Unit,
    navBackStack: NavBackStack
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->


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
            geminiViewModel.creatingQuizzs.collectAsState().value
//
        Log.d("ExtractedTexting",extractedTextFromPdfSucessfull.toString())

        if (extractedTextFromPdfSucessfull){
            navBackStack.add(QuizzRoutes.QuizzPannel)
//            navBackStack.remove(QuizzRoutes.QProcessingScreen)
        }
        LaunchedEffect(pdfUri) {
            if (pdfUri != null) {


//                geminiViewModel.generateQuizz()


//                geminiViewModel.parsePeopleJson(json)


                // Simulate processing steps
                for (i in processingSteps.indices) {
                    processingStep = i
                    delay(1000) // Simulate processing time
                }
                // Generate sample quiz (replace with actual AI generation)
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

//                        Icon(
//                            imageVector = Icons.Default.Add,
//                            contentDescription = "Processing",
//                            modifier = Modifier.size(40.dp),
//                            tint = Color(0xFF667eea)
//                        )
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

//                    ContainedLoadingIndicator(
////                        polygons = listOf(
////                            MaterialShapes.Pill,
////                            MaterialShapes.Oval
////                        ),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(8.dp)
//                            .clip(RoundedCornerShape(4.dp)),
//                        color = Color(0xFF667eea),
//                        progress = (processingStep + 1).toFloat() / processingSteps.size,
//                        containerColor = TODO(),
//                        indicatorColor = TODO(),
//                        containerShape = TODO(),
//                        polygons = TODO(),
////                        containerColor = TODO(),
////                        indicatorColor = TODO(),
////                        containerShape = TODO()
//                    )


//
                    LinearProgressIndicator(
                        progress = (processingStep + 1).toFloat() / processingSteps.size,
                        modifier = Modifier
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
                    // TODO backing
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

    fun generateSampleQuiz(): Quiz {
        return Quiz(
            title = "Sample Quiz",
            questions = listOf(
                Question(
                    id = 1,
                    question = "What is the primary purpose of machine learning?",
                    options = listOf(
                        "To replace human intelligence",
                        "To enable computers to learn from data",
                        "To create artificial consciousness",
                        "To automate all tasks"
                    ),
                    correctAnswer = 1
                ),
                Question(
                    id = 2,
                    question = "Which programming language is most commonly used for AI development?",
                    options = listOf(
                        "Java",
                        "C++",
                        "Python",
                        "JavaScript"
                    ),
                    correctAnswer = 2
                ),
                Question(
                    id = 3,
                    question = "What does 'supervised learning' mean in machine learning?",
                    options = listOf(
                        "Learning without any data",
                        "Learning with labeled training data",
                        "Learning by trial and error",
                        "Learning from unlabeled data"
                    ),
                    correctAnswer = 1
                )
            )
        )
    }


}

fun calculateScore(questions: List<Question>, selectedAnswers: Map<Int, Int>): Int {
    var correct = 0
    questions.forEachIndexed { index, question ->
        if (selectedAnswers[index] == question.correctAnswer) {
            correct++
        }
    }
    return (correct * 100 / questions.size)
}