package com.example.aistudyplanner.Gemini

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

    val tipReply = MutableStateFlow<String>("")

    val isLoading = MutableStateFlow<Boolean>(false)

    val isError = MutableStateFlow<String>("")
    fun sendPrompt(prompt: String){
        isLoading.value = true

        try {

            viewModelScope.launch {
                val response = model.generateContent(prompt)
                tipReply.value = response.text.toString()

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

    val AiTipofTheDay = MutableStateFlow<String>("")
    val tipLoading = MutableStateFlow<Boolean>(false)

    fun aiTipOfTheDay(){
        tipLoading.value = true

        try {

            viewModelScope.launch {
                val aiResponse = model.generateContent("Generate a short, actionable 'Tip of the Day' for students using AI to improve their learning or productivity and to motivate them for more Studying. Keep it under 25 words, friendly, and concise. Avoid complex sentences. Example: 'Use AI to summarize your study materials and generate quizzes to test your knowledge.' Return only the tip text, no extra commentary.")
                tipReply.value = aiResponse.text.toString()

                if (aiResponse.text.toString().isNotEmpty()){
                    tipLoading.value = false

                } else {
                    tipLoading.value = false
                }
            }
        }
        catch (e: Exception){
            isError.value = e.message.toString()
            tipLoading.value = true
        }

    }


}