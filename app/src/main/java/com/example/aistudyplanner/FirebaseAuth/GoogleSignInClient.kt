package com.example.aistudyplanner.FirebaseAuth

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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

    val isSuceessFullLogin = MutableStateFlow<Boolean>(false)


    fun isSingedIn(): Boolean {
        if (auth.currentUser != null) {
            return true
        } else return false
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun signIn() {

        try {
            val result = buildCredentialRquest()
            return handleSignIn(result)


        } catch (e: Exception) {
            if (e is CancellationException) throw e
            println("$tag error occured ${e.message}")

        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {

                val tokenCredential = GoogleIdTokenCredential.Companion.createFrom(credential.data)
                println(tag + "name: ${tokenCredential.displayName}")
                println(tag + "email: ${tokenCredential.id}")
                println(tag + "Picture: ${tokenCredential.profilePictureUri}")

                isSuceessFullLogin.value = !isSuceessFullLogin.value

                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)



                val authResult = auth.signInWithCredential(authCredential).await()

            } catch (e: GoogleIdTokenParsingException) {
                println(tag + e.message)

            }
        } else {
            println(tag + "Credentail is not GoogleIdTokenCrednetial")
            isSuceessFullLogin.value = !isSuceessFullLogin.value

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