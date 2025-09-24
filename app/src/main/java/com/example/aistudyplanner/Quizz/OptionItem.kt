package com.example.aistudyplanner.Quizz

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
private fun OptionItem(
    option: String,
    optionIndex: Int,
    isSelected: Boolean,
    isCorrect: Boolean,
    showAnswer: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        showAnswer && isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.2f)
        showAnswer && isSelected && !isCorrect -> Color(0xFFFF5722).copy(alpha = 0.2f)
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    val borderColor = when {
        showAnswer && isCorrect -> Color(0xFF4CAF50)
        showAnswer && isSelected && !isCorrect -> Color(0xFFFF5722)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (borderColor != Color.Transparent) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Option Label
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        when {
                            showAnswer && isCorrect -> Color(0xFF4CAF50)
                            showAnswer && isSelected && !isCorrect -> Color(0xFFFF5722)
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.outline
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (showAnswer && (isCorrect || (isSelected && !isCorrect))) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.Add else Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = ('A' + optionIndex).toString(),
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Option Text
            Text(
                text = option,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Correct Answer Indicator
            if (showAnswer && isCorrect) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Correct Answer",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun DEPOEFJEG() {

    OptionItem(
        option = "Rohan",
        optionIndex = 2,
        isSelected =false,
        isCorrect = false,
        showAnswer = true,
        onClick = {

        }
    )
}