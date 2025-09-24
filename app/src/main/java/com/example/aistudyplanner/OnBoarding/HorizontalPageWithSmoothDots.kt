package com.example.aistudyplanner.OnBoarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.example.aistudyplanner.ui.theme.CDotUnFocusedColour
import com.google.firebase.crashlytics.FirebaseCrashlytics


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithSmoothDots(firebaseAuthViewModel: FirebaseAuthViewModel,mainNavBackStack: NavBackStack) {
    val pageCount = 3

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })

    val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    Column(
        modifier = Modifier
            .background(CBackground)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            repeat(pageCount) { index ->

                firebaseCrashlytics.log("reapeating")

                val isSelected = pagerState.currentPage == index
                val color by animateColorAsState(
                    targetValue = if (isSelected) CDotFocusedColor else CDotUnFocusedColour,
                    label = ""
                )
                val size by animateDpAsState(
                    targetValue = if (isSelected) 12.dp else 8.dp,
                    label = ""
                )
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(size)
                        .background(color, shape = CircleShape)
                )
            }
        }


        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->

            when (page) {
                0 -> {
                    OnBoardingScreen1(pagerState, page)
                    firebaseCrashlytics.log("Going to onBoardingScreen 1")
                }
                1 -> {
                    OnBoardingScreen2(pagerState, page)
                    firebaseCrashlytics.log("Going to onBoardingScreen 2")
                }
                2 -> {
                    OnBoardingScreen3(
                        firebaseAuthViewModel,
                        mainNavBackStack = mainNavBackStack,
                    )

                    firebaseCrashlytics.log("Going to onBoardingScreen 3")
                }
            }
        }
        // Smooth Dots Indicator
    }
}

