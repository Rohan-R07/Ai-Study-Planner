package com.example.aistudyplanner.Utils

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun AiTipCard(
//    tipTitle: String = "AI Tip of the Day",
//    tipDescription: String = "Use AI to summarize your study materials and generate quizzes to test your knowledge.",
//    onRefresh: () -> Unit
//) {
//    // Glow animation (cycling through RGB)
//    val infiniteTransition = rememberInfiniteTransition()
//    val glowColor by infiniteTransition.animateColor(
//        initialValue = Color(0xFF6A5ACD), // Start: purple
//        targetValue = Color(0xFF00FFFF),  // End: cyan
//        animationSpec = infiniteRepeatable(
//            animation = tween(2500, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
//
//    Box(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .shadow(
//                elevation = 20.dp,
//                spotColor = glowColor,
//                shape = RoundedCornerShape(20.dp)
//            )
//            .background(
//                color = Color(0xFF1A1A2E),
//                shape = RoundedCornerShape(20.dp)
//            )
//            .padding(20.dp)
//    ) {
//        // Refresh button top-right
//        IconButton(
//            onClick = onRefresh,
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .size(28.dp)
//        ) {
//            androidx.compose.material3.Icon(
//                imageVector = Icons.Default.Refresh,
//                contentDescription = "Refresh",
//                tint = Color.White
//            )
//        }
//
//        Column {
//            Text(
//                text = tipTitle,
//                style = MaterialTheme.typography.headlineSmall.copy(
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold
//                )
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = tipDescription,
//                style = MaterialTheme.typography.bodyMedium.copy(
//                    color = Color(0xFFB0B0B0)
//                )
//            )
//        }
//    }
//}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AiTipCard(
    tipTitle: String = "AI Tip of the Day",
    tipDescription: String = "Use AI to summarize your study materials and generate quizzes to test your knowledge.",
    onRefresh: () -> Unit
) {
    // Vibrant RGB glow animation
    val infiniteTransition = rememberInfiniteTransition()
    val glowColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFFF005C), // Neon pink
        targetValue = Color(0xFF00F5FF),  // Electric cyan
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(
                elevation = 30.dp,
                spotColor = glowColor,
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Color(0xFF111111), // Keep this dark for contrast
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        // Refresh Button (top-right)
        IconButton(
            onClick = onRefresh,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(28.dp)
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = glowColor // Matches the glow
            )
        }

        Column {
            Text(
                text = tipTitle,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tipDescription,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFE0E0E0)
                ),
                fontSize = 15.sp
            )
        }
    }
}
