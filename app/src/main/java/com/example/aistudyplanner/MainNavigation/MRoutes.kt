package com.example.aistudyplanner.MainNavigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface MRoutes : NavKey {

    @Serializable
    data object SplashScreen : MRoutes

    @Serializable
    data object OnBoardingScreen : MRoutes

    @Serializable
    data object MainScreen : MRoutes

    @Serializable
    data object SettingsScreen : MRoutes

}