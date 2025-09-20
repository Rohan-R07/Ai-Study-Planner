package com.example.aistudyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.MainNavigation.MRoutes
import com.example.aistudyplanner.MainNavigation.MainNavigation
import com.example.aistudyplanner.ui.theme.AIStudyPlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val geminiViewModel = viewModels<GeminiViewModel>(
            factoryProducer = {
                object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return GeminiViewModel(applicationContext) as T
                    }
                }
            }
        )


        val googleViewModel = viewModels<FirebaseAuthViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return FirebaseAuthViewModel(
                            applicationContext
                        )
                                as T
                    }
                }
            }
        )



        enableEdgeToEdge()
        setContent {
            AIStudyPlannerTheme {
                val mainNavBackStack = rememberNavBackStack<MRoutes>(MRoutes.SplashScreen)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    MainNavigation(
                        mainNavBackStack,
                        applicationContext,
                        googleViewModel.value,
                        innerPadding,
                        geminiViewModel.value,
                        application
                    )
                }
            }
        }
    }
}
