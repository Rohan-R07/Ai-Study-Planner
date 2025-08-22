package com.example.aistudyplanner.OnBoarding

import android.os.Build
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.R
import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.example.aistudyplanner.ui.theme.SignInWIthEmail
import com.example.aistudyplanner.ui.theme.SignInWithGoogle
import com.google.firebase.ai.type.content
import com.google.firebase.logger.Logger


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun OnBoardingScreen3(viewModel: FirebaseAuthViewModel) {

//    val isLoading = viewModel.isSuccessful.collectAsState()
//    val isLoggedIn = viewModel.currentUser?.email?.isNotEmpty()

    val isLoggedIn = viewModel.isSucess.collectAsState()
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
                viewModel.signInWithGoogle()
                if (isLoggedIn.value) {

                    //            Log.d("GoogleSignIN","Woring properly")

                    // Naviation to be done here

                    println("GoogleSignIfffffNClient: working Properly")
                } else  {
//            Log.d("GoogleSignIN","Not working properly")
                    println("GoogleSignINfffClient: No working proplery")
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
}
