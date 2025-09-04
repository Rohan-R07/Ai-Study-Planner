package com.example.aistudyplanner.Gemini

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aistudyplanner.Quizz.Question
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeminiViewModel(
    val appContext: android.content.Context
) : ViewModel() {

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel("gemini-1.5-flash")

    val tipReply = MutableStateFlow<String>("")

    val isLoading = MutableStateFlow<Boolean>(false)

    val isError = MutableStateFlow<String>("")
    fun sendPrompt(prompt: String) {
        isLoading.value = true

        try {

            viewModelScope.launch {
                val response = model.generateContent(prompt)
                tipReply.value = response.text.toString()

                if (response.text.toString().isNotEmpty()) {
                    isLoading.value = false

                } else {
                    isLoading.value = false
                }
            }
        } catch (e: Exception) {
            isError.value = e.message.toString()
            isLoading.value = true
        }
    }

    val AiTipofTheDay = MutableStateFlow<String>("")
    val tipLoading = MutableStateFlow<Boolean>(false)

    fun aiTipOfTheDay() {
        tipLoading.value = true

        try {

            viewModelScope.launch {
                val aiResponse =
                    model.generateContent("Generate a short, actionable 'Tip of the Day' for students using AI to improve their learning or productivity and to motivate them for more Studying. Keep it under 25 words, friendly, and concise. Avoid complex sentences. Example: 'Use AI to summarize your study materials and generate quizzes to test your knowledge.' Return only the tip text, no extra commentary.")
                tipReply.value = aiResponse.text.toString()

                if (aiResponse.text.toString().isNotEmpty()) {
                    tipLoading.value = false

                } else {
                    tipLoading.value = false
                }
            }
        } catch (e: Exception) {
            isError.value = e.message.toString()
            tipLoading.value = true
        }

    }

    // Summarize from youtube video

    val youtubeSummaryResponse = MutableStateFlow<String>("")
    val youtubeSummaryisLoading = MutableStateFlow<Boolean>(true)

    val youtubeSummaryError = MutableStateFlow<Boolean>(false)

    fun YTSummaries(url: String) {
        youtubeSummaryisLoading.value = true
        youtubeSummaryError.value = false

        try {

            viewModelScope.launch {


                val prompt =
                    "Summarize this YouTube video in a clear and concise manner: [${url}]. Include the main points, key insights, and any actionable steps or tips provided in the video. Keep the summary under 200 words and structure it with bullet points for clarity"

                val aiResponse = model.generateContent(prompt)

                youtubeSummaryResponse.value = aiResponse.text.toString()

                if (aiResponse.text.toString().isNotEmpty()) {
                    youtubeSummaryisLoading.value = false

                } else {
                    youtubeSummaryisLoading.value = true
                }
            }
        } catch (e: Exception) {
            youtubeSummaryError.value = true
            tipLoading.value = true
        }
    }



    private val _selectedPdfUri = MutableStateFlow<Uri?>(null)
    val selectedPdfUri: StateFlow<Uri?> = _selectedPdfUri

    private val _extractedText = MutableStateFlow<String?>(null)
    val extractedText: StateFlow<String?> = _extractedText

    private val _summary = MutableStateFlow<String?>(null)
    val summary: StateFlow<String?> = _summary

    fun setPdfUri(uri: Uri) {
        _selectedPdfUri.value = uri
    }

    val isLoadingPdfSummary = MutableStateFlow<Boolean>(false)

    private val isExtractionSucesfull = MutableStateFlow<Boolean>(false)

    fun extractAndSummarize() {
        isLoadingPdfSummary.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val uri = _selectedPdfUri.value ?: return@launch
            try {
                appContext.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val pdfDoc = PDDocument.load(inputStream)
                    val stripper = PDFTextStripper()
                    val text = stripper.getText(pdfDoc)
                    pdfDoc.close()
                    _extractedText.value = text
                    Log.d("Extracted", text.toString())

                    if (text.isNotEmpty()) {
                        isExtractionSucesfull.value = true
                    } else {
                        isExtractionSucesfull.value = false
                    }

                }
            } catch (e: Exception) {
                _extractedText.value = "Failed to extract text: ${e.message}"

                isExtractionSucesfull.value = false

            }


            if (isExtractionSucesfull.value) {

                viewModelScope.launch(Dispatchers.IO) {
                    val text = _extractedText.value ?: return@launch
                    try {
                        val prompt = "Summaizw this pdf content $text"

                        val response = model.generateContent(prompt)
                        _summary.value = response.text.toString()
                        isLoadingPdfSummary.value = false
                    } catch (e: Exception) {
                        _summary.value = "Failed to summarize: ${e.message}"
                        isLoadingPdfSummary.value = true
                    }
                }
            } else {

            }
        }
    }


    val _quizzQuestions = MutableStateFlow<List<Question>>(emptyList())
    val quizzQustion : StateFlow<List<Question>> = _quizzQuestions



    private val _isLoadingQuizz = MutableStateFlow(false)
    val isLoadingQuizz: StateFlow<Boolean> = _isLoadingQuizz

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val generateQuizzUriPDf = MutableStateFlow<Uri?>(null)

    val extractingFromPdfQuizSucessfull = MutableStateFlow<Boolean>(false)

    val extractingTextFromPdfQuizz = MutableStateFlow<String>("")

    val pdfSetingQuizz = MutableStateFlow<Uri?>(null)

    fun setPDfquizz(uri: Uri) {
        pdfSetingQuizz.value = uri
    }

    fun generateQuizz(){

//        _selectedPdfUri.value = uri
        extractingFromPdfQuizSucessfull.value = true
        _isLoadingQuizz.value = true // becomes false just after quizz sucessfulll created

        viewModelScope.launch(Dispatchers.IO) {

            val uri = pdfSetingQuizz.value ?: return@launch

            try {
                appContext.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val pdfDoc = PDDocument.load(inputStream)
                    val stripper = PDFTextStripper()
                    val text = stripper.getText(pdfDoc)
                    pdfDoc.close()
                    extractingTextFromPdfQuizz.value = text
                    extractingFromPdfQuizSucessfull.value = false

                    Log.d("Extracted", text.toString())

                    if (text.isNotEmpty()) {
                        extractingFromPdfQuizSucessfull.value = false
                    } else {
                        extractingFromPdfQuizSucessfull.value = true
                    }

                }
            } catch (e: Exception) {
                extractingTextFromPdfQuizz.value = "Failed to extract text: ${e.message}"

                extractingFromPdfQuizSucessfull.value = false

            }
        }
    }
}


