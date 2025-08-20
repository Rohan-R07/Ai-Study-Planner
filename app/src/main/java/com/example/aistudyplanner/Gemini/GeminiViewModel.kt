package com.example.aistudyplanner.Gemini

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GeminiViewModel : ViewModel() {

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    )
        .generativeModel("gemini-1.5-flash")

    val repllyFromAI = MutableStateFlow<String>("")

    val isLoading = MutableStateFlow<Boolean>(false)

    val isError = MutableStateFlow<String>("")
    fun sendPrompt(prompt: String){
        isLoading.value = true

        try {

            viewModelScope.launch {
                val response = model.generateContent(prompt)
                repllyFromAI.value = response.text.toString()

                if (response.text.toString().isNotEmpty()){
                    isLoading.value = false

                } else {
                    isLoading.value = false
                }
            }

        }
        catch (e: Exception){

            isError.value = e.message.toString()
            isLoading.value = true

        }


    }


}