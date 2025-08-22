package com.example.aistudyplanner

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.MainNavigation.MRoutes
import kotlinx.coroutines.delay

//@Composable
//fun SplashScreen(
//    mainBackStack: NavBackStack,
//    firebaseAuthViewModel: FirebaseAuthViewModel
//) {
//    val scale = remember { Animatable(0f) }
//    val alpha = remember { Animatable(0f) }
//
//    // Gradient background colors
//    val gradientColors = listOf(
//        Color(0xFF6A11CB), // Purple
//        Color(0xFF2575FC)  // Blue
//    )
//
//    // Launch animations & navigation
//    LaunchedEffect(Unit) {
//        scale.animateTo(
//            targetValue = 1f,
//            animationSpec = tween(durationMillis = 1000, easing = { OvershootInterpolator(4f).getInterpolation(it) })
//        )
//        alpha.animateTo(
//            targetValue = 1f,
//            animationSpec = tween(durationMillis = 1200)
//        )
//
//        delay(2000) // Total splash delay
//
//        if (firebaseAuthViewModel.currentUser != null) {
//            mainBackStack.removeAll { true }
//            mainBackStack.add(MRoutes.HomeScreen)
//        } else {
//            mainBackStack.removeAll { true }
//            mainBackStack.add(MRoutes.OnBoardingScreen)
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(gradientColors)
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            // Logo animation
//            Icon(
//                painter = painterResource(id = R.drawable.outline_book_5_24), // Replace with your app logo
//                contentDescription = "AI Study Planner Logo",
//                tint = Color.White,
//                modifier = Modifier
//                    .size(120.dp)
//                    .scale(scale.value)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // App name animation
//            Text(
//                text = "AI Study Planner",
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White.copy(alpha = alpha.value),
//                fontFamily = FontFamily(Font(R.font.space_grotesk))
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = "Smarter learning starts here",
//                fontSize = 16.sp,
//                color = Color.White.copy(alpha = alpha.value * 0.8f)
//            )
//        }
//    }
//}


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
            mainBackStack.add(MRoutes.HomeScreen)
        } else {
            mainBackStack.removeAll { true }
            mainBackStack.add(MRoutes.OnBoardingScreen)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF0F0C29)
                    ),
                    center = Offset(600f, 1200f),
                    radius = 1800f
                )
            ),
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
