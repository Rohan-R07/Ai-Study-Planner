package com.example.aistudyplanner.FirebaseAuth

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class FirebaseAuthViewModel(context: Context) : ViewModel() {

    private val googleSignInClient = GoogleSignInClient(
        context = context
    )

    val Auth = Firebase.auth

    val currentUser = Auth.currentUser

    fun isSignedIn(): Boolean {
        return googleSignInClient.isSingedIn()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun SignInWithGoogle() {
        viewModelScope.launch {
            googleSignInClient.signIn()
        }
    }


    fun googleSignOut() {
        Auth.signOut()
    }
}