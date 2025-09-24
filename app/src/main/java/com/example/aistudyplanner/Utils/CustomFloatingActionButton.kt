package com.example.aistudyplanner.Utils

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.google.firebase.crashlytics.FirebaseCrashlytics


data class FabAction(
    val icon: Painter,
    val label: String,
    val onClick: () -> Unit = {}
)



// Expandable FAB Component
@Composable
fun ExpandableFloatingActionButton(
    fabActions: List<FabAction>,
    modifier: Modifier = Modifier,
    fabColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    fabIcon: ImageVector = Icons.Default.Add,
    fabContentDescription: String = "Quick Actions",
    showLabels: Boolean = true
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "fab_rotation"
    )

    val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom)
    ) {
        // Expanded action items
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(300)) +
                    slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { it / 2 }
                    ),
            exit = fadeOut(animationSpec = tween(200)) +
                    slideOutVertically(
                        animationSpec = tween(200),
                        targetOffsetY = { it / 2 }
                    )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                fabActions.forEach { action ->
                    firebaseCrashlytics.log("Triggring Custom Floating action button")
                    FabActionRow(
                        action = action,
                        showLabel = showLabels
                    )
                }
            }
        }

        // Main FAB
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            containerColor = fabColor,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = fabIcon,
                contentDescription = fabContentDescription,
                modifier = Modifier.rotate(rotationAngle)
            )
        }
    }
}

@Composable
private fun FabActionRow(
    action: FabAction,
    showLabel: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Action label
        if (showLabel) {
            Card(
                modifier = Modifier.wrapContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = CDotFocusedColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(8.dp),

            ) {


                    Text(
                        fontSize = 14.sp,
                        text = action.label,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(Unspecified),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = White,
                    )

            }
        }

        // Action button
        SmallFloatingActionButton(
            onClick = {
                action.onClick()
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Icon(
                painter = action.icon,
                contentDescription = action.label,
                modifier = Modifier.size(20.dp),
                tint = White
            )
        }
    }
}


