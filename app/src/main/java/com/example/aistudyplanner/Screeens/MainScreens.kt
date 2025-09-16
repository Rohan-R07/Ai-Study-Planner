package com.example.aistudyplanner.Screeens

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.aistudyplanner.BottomNavigation.BRoutes
import com.example.aistudyplanner.BottomNavigation.BotttomNavGrpah
import com.example.aistudyplanner.BottomNavigation.bottomNavBarItems
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.Gemini.GeminiViewModel
import com.example.aistudyplanner.PdfSumScreen
import com.example.aistudyplanner.QuizzScreen
import com.example.aistudyplanner.R
import com.example.aistudyplanner.Utils.ExpandableFloatingActionButton
import com.example.aistudyplanner.Utils.FabAction
import com.example.aistudyplanner.YtSummari
//import com.example.aistudyplanner.Utils.ExpandableFab

import com.example.aistudyplanner.ui.theme.CBackground
import com.example.aistudyplanner.ui.theme.CDotFocusedColor
import kotlin.jvm.java

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreens(
    geminiViewModel: GeminiViewModel,
    application: Application
) {

    val bottomNavBackStack = rememberNavBackStack<BRoutes>(BRoutes.HomeScreen)

    val navIndex = remember { mutableStateOf(0) }
    val context = LocalContext.current

    val ytIntent = Intent(context, YtSummari::class.java)
    ytIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val pdfIntent = Intent(context, PdfSumScreen::class.java)
    pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val quizzIntent = Intent(context, QuizzScreen::class.java)
    quizzIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


    Scaffold(
        floatingActionButton = {

            ExpandableFloatingActionButton(
                fabActions = listOf(
                    FabAction(painterResource(R.drawable.youtube_icon), "Summarize from Youtube") {
                        context.startActivity(ytIntent)
                    },
                    FabAction(painterResource(R.drawable.pdf_image), "Summarize with PDFs") {

                        context.startActivity(pdfIntent)

                    },
                    FabAction(painterResource(R.drawable.quizz_icon), "Create an Ai Quizz") {

                        context.startActivity(quizzIntent)

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
            bbackstack = bottomNavBackStack,
            application = application,
            geminiViewModel = geminiViewModel,
            innerPadding = innerpadding
        )
    }
}