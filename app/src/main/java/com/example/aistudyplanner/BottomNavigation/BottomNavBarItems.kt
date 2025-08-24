package com.example.aistudyplanner.BottomNavigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavBarItems (
    val icon: ImageVector,
    val title: String,
    val navRoute: BRoutes
)

val bottomNavBarItems = listOf(
    BottomNavBarItems(
        icon = Icons.Default.Home,
        title = "Home",
        navRoute = BRoutes.HomeScreen
    ),

    BottomNavBarItems(
        icon = Icons.Default.Person,
        title = "Settings",
        navRoute = BRoutes.SettingsScreen
    ),

    BottomNavBarItems(
        icon = Icons.Default.Settings,
        title = "Settings",
        navRoute = BRoutes.SettingsScreen
    ),

)