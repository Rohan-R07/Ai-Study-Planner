package com.example.aistudyplanner.Screeens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.MainNavigation.MRoutes
import com.example.aistudyplanner.ui.theme.CBackground
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    mainBackStack: NavBackStack,
    firebaseAuthViewModel: FirebaseAuthViewModel
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Logo + content entry animation
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )

        delay(2500)

        // Navigation logic
        if (firebaseAuthViewModel.currentUser != null) {
            mainBackStack.removeAll { true }
            mainBackStack.add(MRoutes.MainScreen)
        } else {
            mainBackStack.removeAll { true }
            mainBackStack.add(MRoutes.OnBoardingScreen)
        }
    }
//    containerColor =

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CBackground.copy(0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Replace with a neon-inspired Lottie animation
//            LottieAnimation(
//                composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.)).value,
//                iterations = LottieConstants.IterateForever,
//                modifier = Modifier
//                    .size(200.dp)
//                    .scale(scale.value)
//            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "AI Study Planner",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFEB5AE5), // Neon magenta
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Learning. Reinvented.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFB8B8FF),
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}
