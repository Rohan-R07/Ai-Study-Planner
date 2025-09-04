package com.example.aistudyplanner.Quizz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel

@Composable
fun QuizPlayScreen(
//    quiz: Quiz,
//    onFinish: (Int) -> Unit,
//    onBack: () -> Unit
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }
    var showResult by remember { mutableStateOf(false) }

    val context = LocalContext.current


    val geminiViewMoedl = viewModel<GeminiViewModel>(
        factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return GeminiViewModel(context) as T
                }
            }
    )


//    val currentQuestion =
//    val progress = (currentQuestionIndex + 1).toFloat() / quiz.totalQuestions


    val quizzState = geminiViewMoedl.quizState.collectAsState()


    when {

        quizzState.value.isLoading -> {
            Box(
                modifier = Modifier
                    .size(30.dp),
            ) {
                CircularProgressIndicator()
            }
        }

        quizzState.value.errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = quizzState.value.errorMessage ?: "Something went wrong",
                    color = Color.Red
                )
            }
        }


        quizzState.value.quiz != null -> {
            QuizContent(
                quiz = quizzState.value.quiz!!,
                onOptionSelected = { questionId, selectedIndex ->
                    // Handle user selection (optional)
//                    geminiViewMoedl..checkAnswer(questionId, selectedIndex)
                    geminiViewMoedl.checkAnswer(questionId, selectedIndex)
                }
            )
        }
    }
}


@Composable
fun QuestionCard(
    question: Question,
    onOptionSelected: (Int, Int) -> Unit
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showAnswer by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Q${question.id}: ${question.question}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            question.options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = index
                            onOptionSelected(question.id, index)
                            showAnswer = true
                        }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == index,
                        onClick = {
                            selectedOption = index
                            onOptionSelected(question.id, index)
                            showAnswer = true
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option)
                }
            }

            if (showAnswer) {
                val isCorrect = selectedOption == question.correctAnswer
                Text(
                    text = if (isCorrect) "Correct!" else "Wrong! Correct answer: ${question.options[question.correctAnswer]}",
                    color = if (isCorrect) Color.Green else Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
                question.explanation?.let {
                    Text(
                        text = "Explanation: $it",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun QuizContent(
    quiz: Quiz,
    onOptionSelected: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = quiz.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        quiz.questions.forEach { question ->
            QuestionCard(question = question, onOptionSelected = onOptionSelected)
        }
    }
}
