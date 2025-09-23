package com.example.aistudyplanner.NestedScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import coil.compose.AsyncImage
import com.example.aistudyplanner.BottomNavigation.BRoutes
import com.example.aistudyplanner.FirebaseAuth.FirebaseAuthViewModel
import com.example.aistudyplanner.MainNavigation.MRoutes
import com.example.aistudyplanner.R
import com.example.aistudyplanner.Utils.placeholderList
import com.example.aistudyplanner.ui.theme.CBackground
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackPressed: () -> Unit,
    onSettingsClicked: () -> Unit,
    mainBackStack: NavBackStack,
    botomNavBackStack: NavBackStack
) {
    // Sample profile data (replace with actual user data)

    val context = LocalContext.current


    val firebaseAuth = viewModel<FirebaseAuthViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FirebaseAuthViewModel(context) as T
            }
        }
    )
    val profileName = firebaseAuth.Auth.currentUser?.displayName

    val profileEmail = firebaseAuth.Auth.currentUser?.email

    val profileImageUrl = firebaseAuth.Auth.currentUser?.photoUrl // Add your profile image URL here

    val isEmailVerified =
        firebaseAuth.Auth.currentUser?.isEmailVerified // Add your profile image URL here

    val isImageLoaded = remember { mutableStateOf(false) }


    val isSignOutGoogle = firebaseAuth.isSucess.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = {

                        mainBackStack.add(MRoutes.SettingsScreen)

                    }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CBackground)
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Profile Picture


                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .border(width = 3.dp, color = White, shape = CircleShape)
                        .size(300.dp)
                        .clip(CircleShape)

                ) {

                    AsyncImage(
                        profileImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        placeholder = painterResource(R.drawable.outline_person_24),
                        onLoading = {
//                        Toast.makeText(context,"Loading Profile Picture", Toast.LENGTH_SHORT).show()
                            isImageLoaded.value = true

                        }
                    )

                    if (isImageLoaded.value) {

                        CircularProgressIndicator()
                        isImageLoaded.value = false
                    }
                }

                Spacer(Modifier.padding(20.dp))
                // Name
                Text(
                    text = profileName.toString(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {


                    Text(
                        text = profileEmail.toString(),
                        fontSize = 20.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Log.d("ProfileUrl", profileImageUrl.toString())

                    Spacer(modifier = Modifier.padding(10.dp))

                    if (isEmailVerified == true) {

                        androidx.compose.material3.Icon(
                            painter = painterResource(R.drawable.tick),
                            contentDescription = null,
                            tint = Unspecified,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(40.dp))




                Spacer(modifier = Modifier.height(30.dp))

                // Sign out button

                Button(
                    onClick = {
                        // TODO sign Out button
                        firebaseAuth.googleSignOut()

                        if (!isSignOutGoogle.value) {
                            mainBackStack.add(MRoutes.OnBoardingScreen)
                            mainBackStack.remove(MRoutes.MainScreen)
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red.copy(alpha = 0.2f),
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {

                    Text(
                        text = "Sign out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Red
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    androidx.compose.material3.Icon(
                        imageVector = Icons.AutoMirrored.Default.ExitToApp,
                        contentDescription = "Edit Profile",
                        modifier = Modifier.size(22.dp),
                        tint = Red
                    )
                }
            }
        }
    }
}
