package com.example.aistudyplanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.ui.theme.AIStudyPlannerTheme
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.example.aistudyplanner.ui.theme.CDotUnFocusedColour
import kotlinx.coroutines.delay

class YtSummari : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val geminiViewModel = viewModels<GeminiViewModel>()

        enableEdgeToEdge()
        setContent {
            AIStudyPlannerTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),

                    ) { innerPadding ->


                    var youtubeUrl by remember { mutableStateOf("") }
                    var showCopySuccess by remember { mutableStateOf(false) }

                    var geminiIsLoading =
                        geminiViewModel.value.youtubeSummaryisLoading.collectAsState().value

                    var summary =
                        geminiViewModel.value.youtubeSummaryResponse.collectAsState().value
                    var isLoading by remember { mutableStateOf(false) }
                    var errorMessage by remember { mutableStateOf("") }

                    val context = LocalContext.current


                    Log.d("GoogleAi", summary.toString())

                    Log.d("googleError", isLoading.toString())
                    Column(
                        modifier = Modifier
                            .fillMaxSize()

                            .padding(innerPadding)
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
                    ) {
                        // Header
                        Text(
                            text = "Lets Summarize \n from Youtube :)",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = CDotFocusedColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 40.dp, bottom = 20.dp),
                            fontSize = 34.sp
                        )
                        Spacer(Modifier.padding(10.dp))

                        Text(
                            text = "Paste a YouTube URL below to get an AI-powered summary",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CDotUnFocusedColour,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 32.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )


                        // URL Input Card with animated RGB gradient border
                        val infiniteTransition = rememberInfiniteTransition(label = "gradient")
                        val animatedFloat by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 3000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "hue"
                        )

                        // Generate RGB colors based on hue rotation
                        fun hueToRgb(hue: Float): Color {
                            val normalizedHue = hue / 60f
                            val c = 1f
                            val x = c * (1 - kotlin.math.abs(normalizedHue % 2 - 1))

                            val (r, g, b) = when (normalizedHue.toInt()) {
                                0 -> Triple(c, x, 0f)
                                1 -> Triple(x, c, 0f)
                                2 -> Triple(0f, c, x)
                                3 -> Triple(0f, x, c)
                                4 -> Triple(x, 0f, c)
                                else -> Triple(c, 0f, x)
                            }

                            return Color(r, g, b, 1f)
                        }

                        val gradientBrush = Brush.sweepGradient(
                            colors = listOf(
                                hueToRgb(animatedFloat),
                                hueToRgb(animatedFloat + 60f),
                                hueToRgb(animatedFloat + 120f),
                                hueToRgb(animatedFloat + 180f),
                                hueToRgb(animatedFloat + 240f),
                                hueToRgb(animatedFloat + 300f),
                                hueToRgb(animatedFloat)
                            )
                        )

                        // Card with animated gradient border
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .background(
                                    brush = gradientBrush,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .padding(2.dp) // This creates the border thickness
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "YouTube URL",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    OutlinedTextField(
                                        value = youtubeUrl,
                                        onValueChange = {
                                            youtubeUrl = it
                                            errorMessage = "" // Clear error when user types
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = {
                                            Text(
                                                text = "https://www.youtube.com/your_video",
                                                color = Gray
                                            )
                                        },
                                        trailingIcon = {
                                            if (youtubeUrl.isNotEmpty()) {
                                                IconButton(
                                                    onClick = {
                                                        youtubeUrl = ""
                                                        summary = ""
                                                        errorMessage = ""
                                                    }
                                                ) {
                                                    Icon(
                                                        Icons.Default.Clear,
                                                        contentDescription = "Clear URL"
                                                    )
                                                }
                                            }
                                        },
                                        singleLine = true,
                                        shape = RoundedCornerShape(8.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )

                                    // Error message
                                    if (errorMessage.isNotEmpty()) {
                                        Text(
                                            text = errorMessage,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Summarize Button
                        Button(
                            onClick = {
                                when {
                                    youtubeUrl.isEmpty() -> {
                                        errorMessage = "Please enter a YouTube URL"
                                    }

                                    !isValidYouTubeUrl(youtubeUrl) -> {
                                        errorMessage = "Please enter a valid YouTube URL"
                                    }

                                    else -> {
                                        isLoading = true
                                        errorMessage = ""

                                        geminiViewModel.value.YTSummaries(url = youtubeUrl)

                                        if (geminiIsLoading) {

                                            isLoading = false
                                        } else {
                                            isLoading = true
                                        }

                                        // Simulate loading for demo
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = !isLoading && youtubeUrl.isNotEmpty(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = CDotFocusedColor,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Processing...",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Generate Summary",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Summary Result Card
                        if (summary.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Summary",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        // Copy button could be added here
//                                        TextButton(
//                                            onClick = { /* Handle copy */ }
//                                        ) {
//                                            Text("Copy")
//                                        }

                                        // Copy button
                                        TextButton(
                                            onClick = {
                                                if (summary.isNotEmpty()) {
                                                    val clipboardManager = context.getSystemService(
                                                        Context.CLIPBOARD_SERVICE
                                                    ) as ClipboardManager
                                                    val clip =
                                                        ClipData.newPlainText("Summary", summary)
                                                    clipboardManager.setPrimaryClip(clip)
                                                    showCopySuccess = true
                                                }
                                            }
                                        ) {
                                            Text(if (showCopySuccess) "Copied!" else "Copy")
                                        }


                                        if (showCopySuccess) {
                                            LaunchedEffect(showCopySuccess) {
                                                delay(2000)
                                                showCopySuccess = false
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = summary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeSummarizerScreen(
    onSummarizeClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {

}

// Helper function to validate YouTube URL
fun isValidYouTubeUrl(url: String): Boolean {
    val youtubeRegex = Regex(
        "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/.+",
        RegexOption.IGNORE_CASE
    )
    return youtubeRegex.matches(url)
}

@Preview(showBackground = true)
@Composable
fun YouTubeSummarizerScreenPreview() {
    MaterialTheme {
        YouTubeSummarizerScreen()
    }
}
