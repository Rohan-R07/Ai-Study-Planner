package com.example.aistudyplanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.MainNavigation.MRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(MainBackStack: NavBackStack,firebaseAuthViewModel: FirebaseAuthViewModel) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Splash Screen", fontSize = 40.sp)


        LaunchedEffect(Unit) {

            delay(1000)

            if (firebaseAuthViewModel.currentUser != null) {
                MainBackStack.removeAll{true}

                MainBackStack.add(MRoutes.HomeScreen)

            } else {
                MainBackStack.removeAll{true}
                MainBackStack.add(MRoutes.OnBoardingScreen)


            }

        }



    }


}