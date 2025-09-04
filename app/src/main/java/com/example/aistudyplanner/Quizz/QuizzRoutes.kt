package com.example.aistudyplanner.Quizz

import androidx.navigation3.runtime.NavKey
import com.example.aistudyplanner.QuizzScreen
import kotlinx.serialization.Serializable
import java.io.Serial

interface QuizzRoutes : NavKey{

    @Serializable
    data object QmainScreen: QuizzRoutes

    @Serializable
    data object QProcessingScreen: QuizzRoutes

    @Serializable
    data object QuizzPannel: QuizzRoutes


}