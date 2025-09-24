package com.example.aistudyplanner.OnBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aistudyplanner.R
import com.example.aistudyplanner.Utils.SwipeButton
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Composable
fun OnBoardingScreen2(pageState: PagerState, page: Int) {

    val firebaseCrashlyics = FirebaseCrashlytics.getInstance()
    firebaseCrashlyics.log("Insider of OnBoardingScreen 2")
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(R.drawable.onboardingpic2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Summarize easily",
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "\"Summarize YouTube videos, PDFs, or notes instantly. Get the key points without wasting hours.\" and more",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(15.dp))

        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = CDotFocusedColor)

        Spacer(Modifier.height(15.dp))

        Text(
            text = "Stay Organized",
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Manage your notes, track your progress, and stay on top of your study goals — all in one place.",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.padding(20.dp))
        SwipeButton()
    }


}
