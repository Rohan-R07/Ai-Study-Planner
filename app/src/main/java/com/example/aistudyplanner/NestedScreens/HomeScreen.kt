package com.example.aistudyplanner.NestedScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.BottomNavigation.BRoutes
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.Utils.AiTipCard

import com.example.aistudyplanner.ui.theme.CBackground

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(bBackStack: NavBackStack) {


    val geminiViewModel = viewModel<GeminiViewModel>()

    geminiViewModel.aiTipOfTheDay()


    val tipResponse = geminiViewModel.tipReply.collectAsState()


    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        containerColor = CBackground.copy(0.4f)
    ) { innerpadding ->


        val refreshButton = remember { mutableStateOf(false) }
        AiTipCard(
            tipDescription = tipResponse.value.toString()
        ) {
            refreshButton.value = true
        }

        val isTipLoading = remember { mutableStateOf(false) }

        if (refreshButton.value) {
            LaunchedEffect(Unit) {
                geminiViewModel.aiTipOfTheDay()
                refreshButton.value = false
            }

        }
    }

}