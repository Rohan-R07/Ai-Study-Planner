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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizPlayScreen(
    quiz: Quiz,
    onFinish: (Int) -> Unit,
    onBack: () -> Unit
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }
    var showResult by remember { mutableStateOf(false) }

    val currentQuestion = quiz.questions[currentQuestionIndex]
    val progress = (currentQuestionIndex + 1).toFloat() / quiz.totalQuestions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                androidx.compose.material3.Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "${currentQuestionIndex + 1} / ${quiz.totalQuestions}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Progress Bar
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.3f)
        )

        // Quiz Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Question Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Question ${currentQuestionIndex + 1}",
                            fontSize = 14.sp,
                            color = Color(0xFF667eea),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentQuestion.question,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333),
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            items(currentQuestion.options.size) { optionIndex ->
                val isSelected = selectedAnswers[currentQuestionIndex] == optionIndex

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedAnswers[currentQuestionIndex] = optionIndex
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            Color(0xFF667eea).copy(alpha = 0.1f)
                        } else {
                            Color.White
                        }
                    ),
                    border = if (isSelected) {
                        BorderStroke(2.dp, Color(0xFF667eea))
                    } else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = if (isSelected) Color(0xFF667eea) else Color.Transparent,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 2.dp,
                                    color = if (isSelected) Color(0xFF667eea) else Color.Gray,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Text(
                                    text = "✓",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = currentQuestion.options[optionIndex],
                            fontSize = 16.sp,
                            color = Color(0xFF333333),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Navigation Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentQuestionIndex > 0) {
                OutlinedButton(
                    onClick = { currentQuestionIndex-- },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Text("Previous")
                }
            } else {
                Spacer(modifier = Modifier.width(80.dp))
            }

            Button(
                onClick = {
                    if (currentQuestionIndex < quiz.totalQuestions - 1) {
                        currentQuestionIndex++
                    } else {
                        val score = calculateScore(quiz.questions, selectedAnswers)
                        onFinish(score)
                    }
                },
                enabled = selectedAnswers.containsKey(currentQuestionIndex),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF667eea)
                )
            ) {
                Text(
                    text = if (currentQuestionIndex < quiz.totalQuestions - 1) "Next" else "Finish",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}