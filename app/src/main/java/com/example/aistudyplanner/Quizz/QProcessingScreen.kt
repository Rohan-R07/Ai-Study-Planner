package com.example.aistudyplanner.Quizz

import android.net.Uri
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun ProcessingScreen(
    pdfUri: Uri?,
    onQuizGenerated: (Quiz) -> Unit,
    onBack: () -> Unit
) {
    var processingStep by remember { mutableStateOf(0) }
    val processingSteps = listOf(
        "Reading PDF content...",
        "Analyzing document structure...",
        "Generating intelligent questions...",
        "Creating answer options...",
        "Finalizing quiz..."
    )

    LaunchedEffect(pdfUri) {
        if (pdfUri != null) {

            // Simulate processing steps
            for (i in processingSteps.indices) {
                processingStep = i
                delay(1000) // Simulate processing time
            }


            // Generate sample quiz (replace with actual AI generation)
            val sampleQuiz = generateSampleQuiz()
            onQuizGenerated(sampleQuiz)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                        color = Color(0xFF667eea),
                        strokeWidth = 6.dp
                    )
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Processing",
                        modifier = Modifier.size(40.dp),
                        tint = Color(0xFF667eea)
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
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF667eea)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = onBack,
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

fun calculateScore(questions: List<Question>, selectedAnswers: Map<Int, Int>): Int {
    var correct = 0
    questions.forEachIndexed { index, question ->
        if (selectedAnswers[index] == question.correctAnswer) {
            correct++
        }
    }
    return (correct * 100 / questions.size)
}
