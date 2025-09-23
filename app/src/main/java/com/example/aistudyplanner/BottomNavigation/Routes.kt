package com.example.aistudyplanner.BottomNavigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface BRoutes : NavKey{

    @Serializable
    data object HomeScreen : BRoutes

    @Serializable
    data object ProfileScreen: BRoutes




}