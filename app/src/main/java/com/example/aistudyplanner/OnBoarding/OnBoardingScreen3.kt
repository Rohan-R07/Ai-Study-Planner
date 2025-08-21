package com.example.aistudyplanner.OnBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.aistudyplanner.ui.theme.CBackground


@Composable
fun OnBoardingScreen3(pageState: PagerState, page: Int) {

    Column(
        modifier = Modifier
            .background(CBackground)

            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
//        DotIndicator(pageState, 3)

        Text(
            "Welcome to AiStudy 3",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}
