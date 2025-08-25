package com.example.aistudyplanner.Screeens

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.activity.compose.ReportDrawn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.aistudyplanner.BottomNavigation.BRoutes
import com.example.aistudyplanner.BottomNavigation.BottomNavBarItems
import com.example.aistudyplanner.BottomNavigation.BotttomNavGrpah
import com.example.aistudyplanner.BottomNavigation.bottomNavBarItems
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.Utils.AiTipCard
import com.example.aistudyplanner.Utils.ExpandableFloatingActionButton
import com.example.aistudyplanner.Utils.FabAction
import com.example.aistudyplanner.YtSummari
import com.example.aistudyplanner.ui.theme.BottomNavBarColor
//import com.example.aistudyplanner.Utils.ExpandableFab

import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import kotlin.concurrent.timerTask
import kotlin.jvm.java

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreens(
    firebaseAuthViewModel: FirebaseAuthViewModel,
    mainNavbackStack: androidx.navigation3.runtime.NavBackStack,
    context: Context
) {

    val bottomNavBackStack = rememberNavBackStack<BRoutes>(BRoutes.HomeScreen)

    val navIndex = remember { mutableStateOf(0) }
    val context = LocalContext.current
    val intent = Intent(context, YtSummari::class.java)

    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


    Scaffold(
        floatingActionButton = {

            ExpandableFloatingActionButton(
                fabActions = listOf(
                    FabAction(Icons.Default.Phone, "Summarize from Youtube"){
                        context.startActivity(intent)
                    },
                    FabAction(Icons.Default.Edit, "Summarize with PDFs"){

                    },
                    FabAction(Icons.Default.Menu, "Create an Ai Quizz"){

                    }
                ),
                fabColor = CDotFocusedColor,
                fabIcon = Icons.Default.Add,

                )
        },

        bottomBar = {

            NavigationBar(
                containerColor = CBackground,
                tonalElevation = 2.dp
            ) {

                bottomNavBarItems.forEachIndexed { index, item ->
                    val isSelected = navIndex.value == index
                    NavigationBarItem(
                        modifier = Modifier
                            .width(40.dp),
                        selected = navIndex.value == index,
                        onClick = {
                            navIndex.value = index
                            bottomNavBackStack.removeAll { true }
                            bottomNavBackStack.add(item.navRoute)

                        },
                        icon = {

                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        },
                        label = {
                            AnimatedVisibility(isSelected) {

                                Text(
                                    item.title.toString(),
                                    fontSize = 15.sp
                                )
                            }
                        }
                    )

                }

            }


        }
    ) { innerpadding ->

        BotttomNavGrpah(
            bottomNavBackStack,
            firebaseAuthViewModel,
            innerpadding
        )
    }
}