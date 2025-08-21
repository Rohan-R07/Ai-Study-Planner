package com.example.aistudyplanner.OnBoarding


import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aistudyplanner.R
import com.example.aistudyplanner.Uitls.SwipeButton
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen1(pageState: PagerState, totalPages: Int) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(R.drawable.onboardingpic1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(40.dp))

        Text(
            text = "AI Study Planner",
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(40.dp))

        Text(
            text = "Welcome to AI Study Planner! Your smart companion to plan, learn and revise - faster than ever",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.space_grotesk)),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(50.dp))

        SwipeButton()
    }
}
