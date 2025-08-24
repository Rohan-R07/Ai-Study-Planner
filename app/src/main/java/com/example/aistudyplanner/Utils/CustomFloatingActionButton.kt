package com.example.aistudyplanner.Utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

//@Composable
//fun CustomFloatingActionButton(
//    expandable: Boolean,
//    onFabClick: () -> Unit,
//    fabIcon: ImageVector
//) {
//    var isExpanded by remember { mutableStateOf(false) }
//
//    val fabWidth by animateDpAsState(
//        targetValue = if (isExpanded) 200.dp else 64.dp,
//        animationSpec =
//            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
//    )
//    val fabHeight by animateDpAsState(
//        targetValue = if (isExpanded) 58.dp else 64.dp,
//        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
//    )
//
//    Column {
//        // Expanded content container (visible on expansion)
//        Box(
//            modifier = Modifier
//                .offset(y = 25.dp)
//                .size(width = fabWidth, height = animateDpAsState(
//                    targetValue = if (isExpanded) 225.dp else 0.dp,
//                    animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)
//                ).value)
//                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(18.dp))
//        ) {
//            // Place expanded action items here
//        }
//
//        FloatingActionButton(
//            onClick = {
//                onFabClick()
//                if (expandable) isExpanded = !isExpanded
//            },
//            modifier = Modifier.size(width = fabWidth, height = fabHeight),
//            shape = RoundedCornerShape(18.dp)
//        ) {
//            Icon(fabIcon, contentDescription = null)
//        }
//    }
//}

//
//@Composable
//fun ExpandableFloatingActionButton(
//    modifier: Modifier = Modifier,
//    mainIcon: ImageVector,
//    expanded: MutableState<Boolean>,
//    onExpandedChange: (Boolean) -> Unit,
//    actions: List<Pair<ImageVector, () -> Unit>>
//) {
//    var isExpanded by remember { mutableStateOf(expanded) }
//
//    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
//
//        AnimatedVisibility(
//            visible = isExpanded.value,
//            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
//            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
//        ) {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.padding(bottom = 72.dp)
//            ) {
//                actions.forEach { (icon, onClick) ->
//                    FloatingActionButton(
//                        onClick = onClick,
//                        shape = RoundedCornerShape(16.dp),
//                        modifier = Modifier.size(48.dp)
//                    ) {
//                        Icon(icon, contentDescription = null)
//                    }
//                }
//            }
//        }
//
//
//        FloatingActionButton(
//            onClick = {
//                isExpanded.value = !isExpanded.value
//                onExpandedChange(isExpanded.value)
//            },
//            shape = RoundedCornerShape(18.dp),
//            modifier = Modifier.size(56.dp)
//        ) {
//            Icon(mainIcon, contentDescription = "Main FAB")
//        }
//    }
//}
@Composable
fun VerticalExpandableFABWithLabels(
    modifier: Modifier = Modifier,
    mainIcon: ImageVector,
    expanded: MutableState<Boolean>,
    onExpandedChange: (Boolean) -> Unit,
    actions: List<Triple<ImageVector, String, () -> Unit>> // Icon + Label + Action
) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {

        val fabHeight = 56
        val spacing = 16 // space between each item

        actions.forEachIndexed { index, (icon, label, onClick) ->
            val offsetY = (fabHeight + spacing) * (index + 1)

            AnimatedVisibility(
                visible = expanded.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = -120, // shifts items left so label is visible
                                y = -offsetY
                            )
                        }
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    IconButton(onClick = onClick) {
                        Icon(icon, contentDescription = label)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Main FAB
        FloatingActionButton(
            onClick = {
                expanded.value = !expanded.value
                onExpandedChange(expanded.value)
            },
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.size(fabHeight.dp)
        ) {
            Icon(mainIcon, contentDescription = "Main FAB")
        }
    }
}
