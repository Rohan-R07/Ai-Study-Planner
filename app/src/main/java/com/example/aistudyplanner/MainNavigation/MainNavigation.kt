package com.example.aistudyplanner.MainNavigation

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.MutableRect
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.aistudyplanner.BottomNavigation.BRoutes
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.OnBoarding.HorizontalPagerWithSmoothDots
import com.example.aistudyplanner.Screeens.MainScreens
import com.example.aistudyplanner.Screeens.SplashScreen
import com.google.firebase.crashlytics.FirebaseCrashlytics

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun MainNavigation(
    mainNavbackStack: NavBackStack,
    context: Context,
    firebaseAuthViewModel: FirebaseAuthViewModel,
    innerpadding: PaddingValues,
    geminiViewModel: GeminiViewModel,
    application: Application
) {
    val current = mainNavbackStack.lastOrNull()

    val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = mainNavbackStack,
        onBack = {
            if (current is MRoutes.SettingsScreen) {
                mainNavbackStack.add(MRoutes.MainScreen)
            } else {

            }
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

            entry<MRoutes.SplashScreen> {
                firebaseCrashlytics.log("Starting with spalsh Screeen")

                SplashScreen(
                    mainNavbackStack,
                    firebaseAuthViewModel = firebaseAuthViewModel
                )
            }


            entry<MRoutes.OnBoardingScreen> {
                firebaseCrashlytics.log("Starting with OnBaording Screen")

                HorizontalPagerWithSmoothDots(
                    firebaseAuthViewModel = firebaseAuthViewModel,
                    mainNavbackStack
                )
            }

            entry<MRoutes.MainScreen> {
                firebaseCrashlytics.log("Starting with Main Screen")


                MainScreens(
                    geminiViewModel,
                    application,
                    mainNavbackStack
                )
            }

            entry<MRoutes.SettingsScreen> {
                firebaseCrashlytics.log("Starting with Setting screen")

                SettingsScreen(
                    mainBackStack = mainNavbackStack
                )
            }
        }

    )

}