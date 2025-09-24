package com.example.aistudyplanner.NestedScreens

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material3.ModalBottomSheet


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import com.example.aistudyplanner.BottomNavigation.BRoutes
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.PdfSumScreen
import com.example.aistudyplanner.QuizzScreen
import com.example.aistudyplanner.R
import com.example.aistudyplanner.Recents.RecentsCard
import com.example.aistudyplanner.Recents.RecentsDataStoreVM
import com.example.aistudyplanner.Recents.RecentsPdf
import com.example.aistudyplanner.Utils.AiTipCard

import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import com.google.firebase.crashlytics.FirebaseCrashlytics

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    bBackStack: NavBackStack,
    geminiViewModel: GeminiViewModel,
    application: Application
) {

    val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    val bottmSheetState = rememberModalBottomSheetState()

    var showBottomSheet by remember { mutableStateOf(false) }

    val tipResponse = geminiViewModel.tipReply.collectAsState()

    val recentsvViewModel = viewModel<RecentsDataStoreVM>(
        factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RecentsDataStoreVM(application) as T

                }
            }
    )
    geminiViewModel.aiTipOfTheDay() // has to commented after some time


    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        containerColor = CBackground.copy(0.4f)
    ) { innerpadding ->


        Column(
            modifier = Modifier
                .padding(innerpadding)
                .fillMaxSize()
        ) {
            val refreshButton = remember { mutableStateOf(false) }
            AiTipCard(
                tipDescription = tipResponse.value.toString()
            ) {
                refreshButton.value = true
                firebaseCrashlytics.log(" Ai Tip card Refresh Button ")
            }

            val isTipLoading = remember { mutableStateOf(false) }
            val listings = recentsvViewModel.recents.collectAsState()

            if (refreshButton.value) {
                LaunchedEffect(Unit) {
                geminiViewModel.aiTipOfTheDay()
                    refreshButton.value = false
                    firebaseCrashlytics.log(" Ai Tip card Refresh Button launched Effects")

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Recents",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                    fontWeight = FontWeight.ExtraBold,
                )

                if (!listings.value.isEmpty()) {

                    Spacer(Modifier.padding(start = 140.dp))

                    FilledTonalButton(
                        onClick = {
                            firebaseCrashlytics.log(" HOme screen Clear button")

                            recentsvViewModel.clearRecents()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red.copy(alpha = 0.2f)
                        )
                    ) {
                        Text("Clear Recents", color = Red)
                    }
                }
            }



            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                if (listings.value.isEmpty()) {
                    item {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .background(CBackground),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            firebaseCrashlytics.log("No Recents")


                            Image(
                                painter = painterResource(R.drawable.empty_illustration),
                                contentDescription = null,

                                modifier = Modifier
                                    .size(200.dp)
                            )

                            Spacer(Modifier.padding(20.dp))
                            Text(
                                text = "No Recents",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                            )
                        }

                    }
                } else {

                    firebaseCrashlytics.log("Recents Present")

                    items(listings.value) { item ->
                        val recentsPDFs = remember { mutableStateOf<RecentsPdf?>(null) }
                        Row {

                            val context = LocalContext.current

                            RecentsCard(
                                recentFile = item,
                                onFileClick = { recentsPDF ->
                                    showBottomSheet = true



                                    recentsPDFs.value = recentsPDF
                                },
                                modifier = Modifier
                            )

                        }


                        val context = LocalContext.current

                        if (showBottomSheet) {
                            firebaseCrashlytics.log("Showing bottom sheet for Summarization/Quizz Generation ")


                            ModalBottomSheet(
                                onDismissRequest = {
                                    showBottomSheet = false
                                },
                                sheetState = bottmSheetState,
                                containerColor = CBackground,
                                sheetGesturesEnabled = false
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        "What do you want to do with this file? ",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.space_grotesk))
                                    )

                                    Text(
                                        text = recentsPDFs.value?.name.toString(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                        color = CDotFocusedColor
                                    )




                                    RecentsCard(
                                        recentFile = RecentsPdf(
                                            uri = recentsPDFs.value?.uri.toString(),
                                            name = "Summarize",
                                            openedAt = 343434L
                                        ),
                                        onFileClick = { recentsPDF ->
                                            firebaseCrashlytics.log("Choosing Summarize card for summarixation of the recents pdf")


                                            val intent = Intent(
                                                context,
                                                PdfSumScreen::class.java
                                            ).apply {

                                                putExtra(
                                                    "URI_KEY",
                                                    recentsPDFs.value?.uri.toString()
                                                )
                                            }
                                            context.startActivity(intent)


                                            Log.d(
                                                "URI", recentsPDFs
                                                    .value?.uri.toString()
                                            )
                                            Log.d(
                                                "URI Name", recentsPDFs
                                                    .value?.name.toString()
                                            )
                                        },
                                        modifier = Modifier,
                                        icon = R.drawable.summaryicon
//                                .padding(10.dp)
                                    )


                                    RecentsCard(
                                        recentFile = RecentsPdf(
                                            uri = recentsPDFs.value?.uri.toString(),
                                            name = "Generate Quizz",
                                            openedAt = 343434L,
                                        ),
                                        onFileClick = { recentsPDF ->
                                            firebaseCrashlytics.log("Choosing generate quizz card for summarixation of the recents pdf")

                                            val intent = Intent(
                                                context,
                                                QuizzScreen::class.java
                                            ).apply {
                                                putExtra(
                                                    "URI_KEY",
                                                    recentsPDFs.value?.uri.toString()
                                                )

                                            }
                                            context.startActivity(intent)

                                        },
                                        modifier = Modifier,
                                        icon = R.drawable.quizz_icopn
                                    )

                                    Spacer(Modifier.padding(10.dp))

                                }
                            }
                        }

                    }

                }

            }
        }

    }
}

//}