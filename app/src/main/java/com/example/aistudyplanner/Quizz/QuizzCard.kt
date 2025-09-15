package com.example.aistudyplanner.Quizz

import android.R
import android.view.Gravity
import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.example.aistudyplanner.ui.theme.CDotUnFocusedColour
import com.example.aistudyplanner.ui.theme.Pink40


@Composable
fun McqCard(
    question: String,
    options: List<String>,
    onOptionSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    index: Int,
    correctAns:(Int) -> Unit,
    correctAnswer: Int,   // 👈 NEW param

) {
    var selectedOption by rememberSaveable (index) { mutableStateOf<String?>(null) }


    Card(
        modifier = modifier
            .width(340.dp)
            .height(500.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Pink40)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Question: ${index + 1}", color = White, fontSize = 13.sp)
            Spacer(Modifier.padding(10.dp))
            Text(text = question, style = MaterialTheme.typography.titleMedium, color = White)

            options.forEachIndexed { optionIndex, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = if (selectedOption == option) null else option
                            onOptionSelected(selectedOption)

                            // ✅ check correctness
                            val isCorrect =
                                if (selectedOption != null && optionIndex == correctAnswer) 1 else 0
                            correctAns(isCorrect)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (selectedOption == option),
                        onClick = {
                            selectedOption = if (selectedOption == option) null else option
                            onOptionSelected(selectedOption)

                            val isCorrect =
                                if (selectedOption != null && optionIndex == correctAnswer) 1 else 0
                            correctAns(isCorrect)
                        }
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp),
                        color = White
                    )
                }
            }
        }
    }
}