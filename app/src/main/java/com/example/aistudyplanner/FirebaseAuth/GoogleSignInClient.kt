package com.example.aistudyplanner.FirebaseAuth

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException


class GoogleSignInClient(
    private val context: Context,

    ) {
    private val tag = "GoogleSignINClient: "
    private val auth = Firebase.auth

    private val credentialManager = CredentialManager.create(context)

    val isSuceessFullLogin = mutableStateOf<Boolean>(false)


    fun isSingedIn(): Boolean {
        if (auth.currentUser != null) {
            return true
        } else return false
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun signIn(): Boolean {

        if (isSingedIn()){
            return true
        }


        try {
            val result = buildCredentialRquest()
            return handleSignIn(result)

            isSuceessFullLogin.value = true

        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("$tag error occured ${e.message}")

//            isSuceessFullLogin.apply {  }
            return false
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse) : Boolean{
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {

                val tokenCredential = GoogleIdTokenCredential.Companion.createFrom(credential.data)
                println(tag + "name: ${tokenCredential.displayName}")
                println(tag + "email: ${tokenCredential.id}")
                println(tag + "Picture: ${tokenCredential.profilePictureUri}")

                isSuceessFullLogin.value = true

                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)



                val authResult = auth.signInWithCredential(authCredential).await()

                return authResult.user != null


            } catch (e: GoogleIdTokenParsingException) {
                println(tag + e.message)
                return false
            }
        } else {
            println(tag + "Credentail is not GoogleIdTokenCrednetial")
            isSuceessFullLogin.value = false
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private suspend fun buildCredentialRquest(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("1054936569995-9d8cs59rdc42421ttjtv7t1pdsrflqat.apps.googleusercontent.com")
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()

        return credentialManager.getCredential(
            context = context,
            request = request
        )

    }

}