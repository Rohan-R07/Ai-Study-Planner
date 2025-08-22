package com.example.aistudyplanner.MainNavigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.OnBoarding.HorizontalPagerWithSmoothDots
import com.example.aistudyplanner.SplashScreen

@Composable
fun MainNavigation(mainNavbackStack: NavBackStack, firebaseAuthViewModel: FirebaseAuthViewModel, innerpadding: PaddingValues) {


    NavDisplay(
        modifier = Modifier.padding(innerpadding),
        backStack = mainNavbackStack,
        onBack = {
            mainNavbackStack.removeLastOrNull()
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
                SplashScreen(
                    mainNavbackStack,
                    firebaseAuthViewModel = firebaseAuthViewModel
                )
            }


            entry<MRoutes.OnBoardingScreen> {
                HorizontalPagerWithSmoothDots(firebaseAuthViewModel = firebaseAuthViewModel,mainNavbackStack)
            }


            entry<MRoutes.HomeScreen> {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {



                    if (firebaseAuthViewModel.isSignedIn())
                        Text("Signed In")
                    else
                        Text("Not Signed In")

                    Button(
                        onClick = {
                            firebaseAuthViewModel.googleSignOut()
                            mainNavbackStack.removeAll { true }
                            mainNavbackStack.add(MRoutes.OnBoardingScreen)
                        }
                    ) {
                        Text("sign Out" )
                    }
                }
            }
        }

    )

}