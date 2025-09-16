package com.example.aistudyplanner.NestedScreens

import android.app.Application
import android.widget.Space
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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
import com.example.aistudyplanner.R
import com.example.aistudyplanner.Recents.RecentsDataStoreVM
import com.example.aistudyplanner.Utils.AiTipCard
import com.example.aistudyplanner.Utils.placeholderList

import com.example.aistudyplanner.ui.theme.CBackground

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    bBackStack: NavBackStack,
    geminiViewModel: GeminiViewModel,
    application: Application
) {


    val tipResponse = geminiViewModel.tipReply.collectAsState()

    val recentsvViewModel = viewModel<RecentsDataStoreVM>(
        factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RecentsDataStoreVM(application) as T

                }
            }
    )


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
            }

            val isTipLoading = remember { mutableStateOf(false) }
            val listings = recentsvViewModel.recents.collectAsState()

            if (refreshButton.value) {
                LaunchedEffect(Unit) {
//                geminiViewModel.aiTipOfTheDay()
                    refreshButton.value = false
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

                if (!listings.value.isEmpty()){

                    Spacer(Modifier.padding(start = 140.dp))

                    FilledTonalButton(
                        onClick = {
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

                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .background(CBackground),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {


                            Image(
                                painter = painterResource(R.drawable.empty_illustration),
                                contentDescription = null,

                                modifier = Modifier
                                    .size(200.dp)
//                                    .offset(y = 50.dp)
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

                    items(listings.value) { item ->

                        Row {

                            Text(item.name, color = White)

                            Spacer(Modifier.padding(10.dp))
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        recentsvViewModel.clearRecents()
                                    }
                            )
                        }

                    }
                }

            }
        }

    }
}

//}