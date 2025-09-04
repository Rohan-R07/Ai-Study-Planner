package com.example.aistudyplanner.Quizz

data class Quiz(
    val title: String,
    val questions: List<Question>,
    val totalQuestions: Int = questions.size
)


data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String? = null
)

