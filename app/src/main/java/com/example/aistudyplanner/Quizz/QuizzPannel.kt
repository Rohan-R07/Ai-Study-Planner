package com.example.aistudyplanner.Quizz

import android.R
import android.util.Log
import android.widget.Button
import androidx.arch.core.executor.TaskExecutor
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.disableHotReloadMode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.example.aistudyplanner.ui.theme.CDotUnFocusedColour
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizzPannel(
    navBackStackEntry: NavBackStack
) {

    val context = LocalContext.current

    val geminiViewMoedl = viewModel<GeminiViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GeminiViewModel(context) as T
        }
    })

    val quizz = geminiViewMoedl.quizState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = quizz.value?.title.toString(), color = White
                    )
                }, colors = TopAppBarColors(
                    containerColor = CBackground,
                    scrolledContainerColor = CBackground,
                    navigationIconContentColor = White,
                    titleContentColor = White,
                    actionIconContentColor = White,
                    subtitleContentColor = White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navBackStackEntry.remove(QuizzRoutes.QuizzPannel)
                        }
                    ) { }
                }
            )
        }, contentColor = CBackground
    ) { innerPadding ->

        var selectedQuestionId by remember { mutableStateOf<Int?>(null) }
        var showAnswers by remember { mutableStateOf(false) }
        var completedQuestions by remember { mutableStateOf(setOf<Int>()) }
        var userAnswers by remember { mutableStateOf(mapOf<Int, Int>()) }

        val stateShifter = remember { mutableStateOf(true) }
        val currentSteps = remember { mutableStateOf(0) }
        val totalSteps = quizz.value?.totalQuestions?.toInt()


        val previousBUttonDisabled = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(CBackground)
        ) {

            Log.d("TotalQuestion", quizz.value?.totalQuestions.toString())

            val listState = rememberLazyListState()

            val lazyRowIndex = remember { mutableIntStateOf(0) }

            val courutoneScop = rememberCoroutineScope()

            AnimatedVisibility(
                stateShifter.value,
                modifier = Modifier.fillMaxSize(),

                ) {


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item {
                        SegmentedProgressBar(
                            totalSegments = totalSteps.toString().toInt(),
                            currentStep = currentSteps.value,
                            filledColor = CDotFocusedColor,
                            emptyColor = CDotUnFocusedColour,
                            modifier = Modifier.padding(20.dp)
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(20.dp),
                            userScrollEnabled = false,
                            state = listState
                        ) {

                            itemsIndexed(quizz.value?.questions ?: emptyList()) { index, question ->

                                Log.d("QUestion", question.question)

                                Spacer(Modifier.padding(9.dp))

                                McqCard(
                                    question = question.question,
                                    options = question.options,
                                    onOptionSelected = {

                                    },
                                    index = index,
                                    modifier = Modifier
                                )

                                Spacer(Modifier.padding(14.dp))
                            }

                        }

                    }




                    item {
                        Row {
                            // Previous Button
                            Button(
                                onClick = {
                                    if (currentSteps.value > 1) {
                                        val targetIndex =
                                            currentSteps.value - 2 // because steps are 1-based
                                        courutoneScop.launch {
                                            listState.animateScrollToItem(targetIndex)
                                        }
                                        currentSteps.value -= 1 // decrement only once
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CDotFocusedColor,
                                    contentColor = White,
                                    disabledContainerColor = Color.Gray,
                                    disabledContentColor = White.copy(alpha = 0.5f)
                                ),
                                enabled = currentSteps.value > 1
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Previous")
                                }
                            }

                            Spacer(Modifier.padding(20.dp))

                            // Next Button
                            Button(
                                onClick = {
                                    if (currentSteps.value < totalSteps.toString().toInt()) {
                                        val targetIndex =
                                            currentSteps.value // because steps start from 1
                                        courutoneScop.launch {
                                            listState.animateScrollToItem(targetIndex)
                                        }
                                        currentSteps.value += 1 // increment only once
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CDotFocusedColor,
                                    contentColor = White
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "Next")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }

                }

            }
            Log.d("TotalLength", totalSteps.toString())
        }
    }
}

@Composable
fun SegmentedProgressBar(
    totalSegments: Int,
    currentStep: Int, // Should now be 1..totalSegments (not 0..totalSegments)
    modifier: Modifier = Modifier,
    filledColor: Color = Color(0xFF4CAF50),
    emptyColor: Color = Color.LightGray,
    segmentSpacing: Dp = 4.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(totalSegments) { index ->
            val shape = when (index) {
                0 -> RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp) // First block
                totalSegments - 1 -> RoundedCornerShape(
                    topEnd = 10.dp, bottomEnd = 10.dp
                ) // Last block
                else -> RectangleShape // Middle blocks
            }
            Log.d("index", index.toString())
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .padding(horizontal = segmentSpacing / 2)
                    .background(
                        color = if (index < currentStep.coerceIn(
                                1, totalSegments
                            )
                        ) filledColor else emptyColor,

                        shape = shape
                    )
            )
        }
    }
}
