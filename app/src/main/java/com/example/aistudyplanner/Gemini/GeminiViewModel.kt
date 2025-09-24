package com.example.aistudyplanner.Gemini

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aistudyplanner.Quizz.Question
import com.example.aistudyplanner.Quizz.Quiz
import com.example.aistudyplanner.Quizz.QuizState
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

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
        firebaseCrashlytics.log("starting with youtube summarization")
        try {

            viewModelScope.launch {


                val prompt =
                    "Summarize this YouTube video in a clear and concise manner: [${url}]. Include the main points, key insights, and any actionable steps or tips provided in the video. Keep the summary under 200 words and structure it with bullet points for clarity"

                val aiResponse = model.generateContent(prompt)

                youtubeSummaryResponse.value = aiResponse.text.toString()

                if (aiResponse.text.toString().isNotEmpty()) {
                    youtubeSummaryisLoading.value = false
                    firebaseCrashlytics.log("youtube summarization Sucessfull")

                } else {
                    youtubeSummaryisLoading.value = true

                    firebaseCrashlytics.log("youtube summarization Failed")

                }
            }
        } catch (e: Exception) {
            youtubeSummaryError.value = true
            tipLoading.value = true

            firebaseCrashlytics.log("An error occured while Summarizing with youtube Link $e")

        }
    }


    val firebaseCrashlytics = FirebaseCrashlytics.getInstance()
    private val _selectedPdfUri = MutableStateFlow<Uri?>(null)
    val selectedPdfUri: StateFlow<Uri?> = _selectedPdfUri

    private val _extractedText = MutableStateFlow<String?>(null)
    val extractedText: StateFlow<String?> = _extractedText

    private val _summary = MutableStateFlow<String?>(null)
    val summary: StateFlow<String?> = _summary

    fun setPdfUri(uri: Uri) {
        _selectedPdfUri.value = uri
        firebaseCrashlytics.log("Setting pdf for summarization")

    }

    val isLoadingPdfSummary = MutableStateFlow<Boolean>(false)

    private val isExtractionSucesfull = MutableStateFlow<Boolean>(false)

    fun extractAndSummarize() {

        firebaseCrashlytics.log("Extracting and summarixation started with the pdf")

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
                        firebaseCrashlytics.log("Extracting and summarixation Sucessfull")
                        isExtractionSucesfull.value = true
                    } else {

                        firebaseCrashlytics.log("Extracting and summarixation Failed")

                        isExtractionSucesfull.value = false
                    }

                }
            } catch (e: Exception) {
                _extractedText.value = "Failed to extract text: ${e.message}"

                isExtractionSucesfull.value = false

                firebaseCrashlytics.log("Error occured while extracting and summarixing from pdf $e")

            }


            if (isExtractionSucesfull.value) {

                viewModelScope.launch(Dispatchers.IO) {
                    val text = _extractedText.value ?: return@launch
                    try {
                firebaseCrashlytics.log("Extraction from the PDF for summarizatoin is sucessfull")
                        val prompt = "Summaizw this pdf content $text"

                        val response = model.generateContent(prompt)
                        _summary.value = response.text.toString()
                        isLoadingPdfSummary.value = false
                    } catch (e: Exception) {

                        firebaseCrashlytics.log("Extraction from the PDF for summarizatoin is Failed $e")

                        _summary.value = "Failed to summarize: ${e.message}"
                        isLoadingPdfSummary.value = true
                    }
                }
            } else {

            }
        }
    }


    val _quizzQuestions = MutableStateFlow<List<Question>>(emptyList())
    val quizzQustion: StateFlow<List<Question>> = _quizzQuestions


    private val _isLoadingQuizz = MutableStateFlow(false)
    val isLoadingQuizz: StateFlow<Boolean> = _isLoadingQuizz

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val generateQuizzUriPDf = MutableStateFlow<Uri?>(null)

    val extractingFromPdfQuizSucessfull = MutableStateFlow<Boolean>(false)

    val extractingTextFromPdfQuizz = MutableStateFlow<String>("")

    val pdfSetingQuizz = MutableStateFlow<Uri?>(null)

    //    private val _quizState = MutableStateFlow(QuizState())
//    val quizState: StateFlow<QuizState> = _quizState
    fun setPDfquizz(uri: Uri) {
        pdfSetingQuizz.value = uri
        firebaseCrashlytics.log("Setting PDF uri for Quizz Generation")
    }

    val creatingQuizzs = MutableStateFlow<Boolean>(false)

    @SuppressLint("SuspiciousIndentation")
    fun generateQuizz() {

        firebaseCrashlytics.log("Starting With Quizz Generation")

        extractingFromPdfQuizSucessfull.value = true
        _isLoadingQuizz.value = true // becomes false just after quizz sucessfulll created

        creatingQuizzs.value = false


        viewModelScope.launch(Dispatchers.IO) {
            creatingQuizzs.value = false

            val uri = pdfSetingQuizz.value ?: return@launch

            try {
                firebaseCrashlytics.log("sucessfully Extracted for quizz")


                appContext.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val pdfDoc = PDDocument.load(inputStream)
                    val stripper = PDFTextStripper()
                    val text = stripper.getText(pdfDoc)
                    pdfDoc.close()
                    creatingQuizzs.value = false

                    Log.d("ExtractingText", text.toString())






                    if (text.isNotEmpty()) {
                        extractingTextFromPdfQuizz.value = text
                        extractingFromPdfQuizSucessfull.value = true
                        creatingQuizzs.value = false
                        firebaseCrashlytics.log("sucessfully Extracted for quizz")


                        val prompt = """
                            
                            this is the thing which i need you to make quizz of 
                            
                            ${text.toString()}

                        {
                          "title": "Your Quiz Title",
                          "questions": [
                            {
                              "id": 1,
                              "question": "What is ...?(max of 20 words)",
                              "options": ["Option 1", "Option 2", "Option 3", "Option 4", "Option5"],
                              "correctAnswer": 2,
                              "explanation": "Explanation for correct answer"
                            }
                          ]
                        }
                        Content:
                        $extractedText
                    """.trimIndent()


                        try {
                            val aiResponse = model.generateContent(prompt)

                            firebaseCrashlytics.log("Passing Extracted Text for Gemini API")

//                            Log.d("Quizzinghere brother",aiResponse.text.toString()   )

                            Log.d("Quizzinghere", aiResponse.text.toString())

                            if (aiResponse.text?.isNotEmpty()!!) {
                                creatingQuizzs.value = true

                                firebaseCrashlytics.log("Sucessfully generated Quizz")

                                loadQuizFromJson(aiResponse.text.toString())
                            } else creatingQuizzs.value = false
                            firebaseCrashlytics.log("Failed to generated quizz")
//                            val quiz = parseQuizJson(json)

//                            withContext(Dispatchers.Main) {
//                                _quizState.value = QuizState(
//                                    quiz = quiz, isLoading = false
//                                )
//                            }

                        } catch (e: Exception) {
                            creatingQuizzs.value = false
                            firebaseCrashlytics.log("Failed to generaed with error code $e")
                        }

                        creatingQuizzs.value = false

                    } else {
                    }
                }
            } catch (e: Exception) {
                firebaseCrashlytics.log("Failed to extract text from the pdf with error code $e")
            }


        }
    }


    val sucessfulyCreatedQuizz = MutableStateFlow<Boolean>(false)
    private val _quizState = MutableStateFlow<Quiz?>(null)
    val quizState: StateFlow<Quiz?> = _quizState

    private val _errorJson = MutableStateFlow<String?>(null)
    val jsonError: StateFlow<String?> = _errorJson

    fun loadQuizFromJson(rawJson: String) {
        viewModelScope.launch(Dispatchers.IO) {

            firebaseCrashlytics.log("Started Loading parsing json from the response")

            sucessfulyCreatedQuizz.value = false
            try {

                firebaseCrashlytics.log("Sucessfully stored json inside of variable with parse JSON methord")

                val cleaned = sanitizeJson(rawJson)
                Log.d("QuizViewModel", "Cleaned JSON: ${cleaned.take(200)}")

                val quizObj = JSONObject(cleaned)
                val title = quizObj.optString("title", "Generated Quiz")
                val questionsArray = quizObj.optJSONArray("questions")
                    ?: JSONArray() // default empty array if missing

                val questionsList = mutableListOf<Question>()
                for (i in 0 until questionsArray.length()) {
                    val qObj = questionsArray.getJSONObject(i)

                    val id = qObj.optInt("id", i + 1)
                    val question = qObj.optString("question", "")
                    val optionsArray = qObj.optJSONArray("options") ?: JSONArray()
                    val options =
                        List(optionsArray.length()) { idx -> optionsArray.optString(idx, "") }
                    val correctAnswer = qObj.optInt("correctAnswer", -1)
                    val explanation = qObj.optString("explanation", null)

                    questionsList.add(
                        Question(
                            id = id,
                            question = question,
                            options = options,
                            correctAnswer = correctAnswer,
                            explanation = explanation
                        )
                    )
                }
                sucessfulyCreatedQuizz.value = true
                _quizState.value = Quiz(title = title, questions = questionsList)



                _errorJson.value = null
                Log.d("QuizViewModel", "Parsed ${questionsList.size} questions")
            } catch (e: Exception) {

                firebaseCrashlytics.log("Failed to stored json inside of variable with parse JSON methord")

                Log.e("QuizViewModel", "Failed to parse JSON", e)
                _errorJson.value = "Failed to parse quiz: ${e.message}"
                _quizState.value = null
                sucessfulyCreatedQuizz.value = false
            }
        }
    }

    private fun sanitizeJson(raw: String): String {

        firebaseCrashlytics.log("Small helper fucntion for converting JSON into Variable")

        var s = raw.trim()
        s = s.replace("```json", "", ignoreCase = true)
            .replace("```", "")
            .trim()

        val first = s.indexOfFirst { it == '{' }
        val last = s.lastIndexOf('}')
        return if (first >= 0 && last >= first) {
            s.substring(first, last + 1)
        } else {
            s
        }
    }
}
