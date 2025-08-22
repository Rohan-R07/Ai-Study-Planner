package com.example.aistudyplanner.OnBoarding

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.MainNavigation.MRoutes
import com.example.aistudyplanner.R
import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.example.aistudyplanner.ui.theme.SignInWIthEmail
import com.example.aistudyplanner.ui.theme.SignInWithGoogle
import com.google.firebase.ai.type.content
import com.google.firebase.logger.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log
import kotlin.math.sign


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun OnBoardingScreen3(viewModel: FirebaseAuthViewModel, mainNavBackStack: NavBackStack) {

    val isLoggedIn = viewModel.isSucess.collectAsState()

    Log.d("GoogleSignINClient", isLoggedIn.value.toString())

    val googleSIgnInState = remember { mutableStateOf(false) }

    val courutine = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .background(CBackground)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        Text(
            text = "Let's Get Started",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.padding(15.dp))

        Text(
            "Sign in to continue your learning journey",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            fontSize = 18.sp
        )

        Spacer(Modifier.padding(10.dp))

        Image(
            painter = painterResource(R.drawable.onboardingpic3),
            contentDescription = null,
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.padding(10.dp))

        Button(
            onClick = {


                courutine.launch {
                    val success = viewModel.signInWithGoogle()
                    if (success) {
                        mainNavBackStack.clear()
                        mainNavBackStack.add(MRoutes.HomeScreen)
                    }
                }

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = SignInWithGoogle,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Sign in with Google",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )


        }


//        if (googleSIgnInState.value){
//
//        }


    }


    Spacer(Modifier.padding(10.dp))

    Text(
        "By Continuing. you agree to our Terms of Serivces and Privacy Policy",
        color = CDotFocusedColor.copy(0.6f),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontFamily = FontFamily(Font(R.font.space_grotesk)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )


}
