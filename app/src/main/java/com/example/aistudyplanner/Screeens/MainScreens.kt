package com.example.aistudyplanner.Screeens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.aistudyplanner.BottomNavigation.BRoutes
import com.example.aistudyplanner.BottomNavigation.BottomNavBarItems
import com.example.aistudyplanner.BottomNavigation.BotttomNavGrpah
import com.example.aistudyplanner.BottomNavigation.bottomNavBarItems
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.Utils.AiTipCard
import com.example.aistudyplanner.Utils.VerticalExpandableFABWithLabels
import com.example.aistudyplanner.ui.theme.CBackground
import kotlin.concurrent.timerTask

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreens(
    firebaseAuthViewModel: FirebaseAuthViewModel,
    mainNavbackStack: androidx.navigation3.runtime.NavBackStack,
) {

    val bottomNavBackStack = rememberNavBackStack<BRoutes>(BRoutes.HomeScreen)

    val navIndex = remember { mutableStateOf(0) }
    val toolbarExpanded = remember { mutableStateOf(false) }

    Scaffold(
//        bottomBar = {
//            NavigationBar(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                containerColor = CBackground,
//                contentColor = Unspecified,
//                tonalElevation = 2.dp,
//            ) {
//
//                bottomNavBarItems.forEachIndexed { index, bottomNavBarItem ->
//
//                    NavigationBarItem(
//                        selected = index == navIndex.value,
//                        onClick = {
//
//                            navIndex.value = index
//                            bottomNavBackStack.add(bottomNavBarItem.navRoute)
//
//
//                        },
//                        icon = {
//                            Icon(
//                                imageVector = bottomNavBarItem.icon,
//                                contentDescription = null
//                            )
//                        },
//                        label = {
//                            Text(
//                                text = bottomNavBarItem.title
//                            )
//                        }
//                    )
//
//                }
//            }
//        },
        floatingActionButton = {
//            VerticalExpandableFABWithLabels(
//                mainIcon = Icons.Default.Add,
//                expanded = toolbarExpanded,
//                onExpandedChange = { toolbarExpanded.value = it },
//                actions = listOf(
//                    Triple(Icons.Default.Edit, "Edit") { /* Edit action */ },
//                    Triple(Icons.Default.Share, "Share") { /* Share action */ },
//                    Triple(Icons.Default.Settings, "Settings") { /* Settings action */ }
//
//                    )
//            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                HorizontalFloatingToolbar(
                    expanded = true,
//                floatingActionButton = {
//                    FloatingToolbarDefaults.VibrantFloatingActionButton(onClick = {
//                        onExpandedChange(!expanded)
//                    }) {
//                        Icon(Icons.Default.Add, contentDescription = null)
//                    }
//                },
                    modifier = Modifier
                        .padding(16.dp),

                    content = {
                        IconButton(onClick = { /* Action 1 */ },
                            modifier = Modifier
                        ) {
                            Row {

                            Icon(Icons.Default.Home, contentDescription = "Edit")
                            }
                        }
                        IconButton(onClick = { /* Action 2 */ }) {
                            Icon(Icons.Default.Settings, contentDescription = "Share")
                        }
                        IconButton(onClick = { /* Action 3 */ }) {
                            Icon(Icons.Default.Person, contentDescription = "Settings")
                        }
                    }
                )
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
