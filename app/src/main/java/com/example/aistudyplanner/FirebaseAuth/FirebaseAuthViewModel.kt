package com.example.aistudyplanner.FirebaseAuth

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.util.fastCbrt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FirebaseAuthViewModel(context: Context) : ViewModel() {

    private val googleSignInClient = GoogleSignInClient(
        context = context
    )

    val Auth = Firebase.auth

    val currentUser = Auth.currentUser

    private var _isSucessfull = googleSignInClient.isSuceessFullLogin.value

    val isSucess = MutableStateFlow<Boolean>(false)

    // State for loading and login success
     val _isLoading = MutableStateFlow(true)


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun signInWithGoogle(): Boolean {
        _isLoading.value = true
        val result = googleSignInClient.signIn() // This is already suspend
        isSucess.value = result
        _isLoading.value = false
        return result
    }


    fun googleSignOut() {
        Auth.signOut()
        _isSucessfull = false
    }
}