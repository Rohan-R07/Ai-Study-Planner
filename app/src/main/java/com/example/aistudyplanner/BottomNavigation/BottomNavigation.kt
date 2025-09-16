package com.example.aistudyplanner.BottomNavigation

import android.app.Application
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.shadow.InnerShadowPainter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.MainNavigation.MRoutes
import com.example.aistudyplanner.NestedScreens.HomeScreen
import com.example.aistudyplanner.NestedScreens.ProfileScreen
import com.example.aistudyplanner.OnBoarding.HorizontalPagerWithSmoothDots
import com.example.aistudyplanner.Screeens.MainScreens
import com.example.aistudyplanner.Screeens.SplashScreen
import com.example.aistudyplanner.mApplication


@Composable
fun BotttomNavGrpah(bbackstack: NavBackStack, application: Application,geminiViewModel: GeminiViewModel,innerPadding: PaddingValues) {


    NavDisplay(
        modifier = Modifier
            .padding(innerPadding)
            .padding(10.dp),
        backStack = bbackstack,
        onBack = {
            bbackstack.removeLastOrNull()
        },
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator(),
        ),

        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        entryProvider = entryProvider {

            entry<BRoutes.HomeScreen> {
                HomeScreen(bbackstack, geminiViewModel =geminiViewModel,application )
            }


            entry<BRoutes.ProfileScreen> {
                ProfileScreen()
            }


            entry<BRoutes.SettingsScreen> {

            }
        }

    )

}