package com.example.aistudyplanner.Quizz

data class Quiz(
    val title: String,
    val questions: List<Question>,
    val totalQuestions: Int = questions.size
)


data class Question(
    val id: Int,
    val question: String,
    val options: List<String> = emptyList(),
    val correctAnswer: Int,
    val explanation: String? = null
)

data class QuizState(
    val quiz: Quiz? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


