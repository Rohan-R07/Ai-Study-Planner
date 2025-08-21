package com.example.aistudyplanner.Uitls

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aistudyplanner.R

//@Composable
//fun SwipeText() {
//    val colors = listOf(Color(0xFF6C63FF), Color(0xFFFF6B6B), Color(0xFF00C896))
//    val infiniteTransition = rememberInfiniteTransition(label = "")
//    val colorIndex = infiniteTransition.animateValue(
//        initialValue = 0,
//        targetValue = colors.size - 1,
//        typeConverter = Int.VectorConverter,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ), label = ""
//    )
//
//    Text(
//        text = "Swipe →",
//        color = colors[colorIndex.value],
//        fontSize = 20.sp,
//        fontWeight = FontWeight.Bold,
//        fontFamily = FontFamily(Font(R.font.space_grotesk))
//    )
//}


@Composable
fun SwipeButton() {
    val colors = listOf(
        Color(0xFFFF6B6B), // Bright Coral Red
        Color(0xFFFFC107), // Vibrant Yellow
        Color(0xFF00E5FF)  // Aqua Cyan
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedColor = infiniteTransition.animateColor(
        initialValue = colors.first(),
        targetValue = colors.last(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(animatedColor.value.copy(alpha = 0.15f)) // Soft playful background
            .border(
                width = 2.dp,
                color = animatedColor.value,
                shape = RoundedCornerShape(50)
            )
            .clickable { /* Handle Swipe Action */ }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Swipe →",
            color = animatedColor.value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.space_grotesk))
        )
    }
}
