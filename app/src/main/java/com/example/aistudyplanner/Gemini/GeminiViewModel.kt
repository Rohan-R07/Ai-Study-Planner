package com.example.aistudyplanner.Gemini

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aistudyplanner.Quizz.Question
import com.example.aistudyplanner.Quizz.Quiz
import com.example.aistudyplanner.Quizz.QuizState
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
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
    val quizzQustion: StateFlow<List<Question>> = _quizzQuestions


    private val _isLoadingQuizz = MutableStateFlow(false)
    val isLoadingQuizz: StateFlow<Boolean> = _isLoadingQuizz

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val generateQuizzUriPDf = MutableStateFlow<Uri?>(null)

    val extractingFromPdfQuizSucessfull = MutableStateFlow<Boolean>(false)

    val extractingTextFromPdfQuizz = MutableStateFlow<String>("")

    val pdfSetingQuizz = MutableStateFlow<Uri?>(null)

    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState
    fun setPDfquizz(uri: Uri) {
        pdfSetingQuizz.value = uri
    }

    val creatingQuizzs = MutableStateFlow<Boolean>(false)

    fun generateQuizz() {

        _quizState.value = _quizState.value.copy(isLoading = true, errorMessage = null)


//        _selectedPdfUri.value = uri
        extractingFromPdfQuizSucessfull.value = true
        _isLoadingQuizz.value = true // becomes false just after quizz sucessfulll created

        creatingQuizzs.value = false


        viewModelScope.launch(Dispatchers.IO) {
            creatingQuizzs.value = false

            val uri = pdfSetingQuizz.value ?: return@launch

            try {
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


                        val prompt = """
                            
                            this is the thing which i need you to make quizz of 
                            
                            ${text.toString()}

                        {
                          "title": "Your Quiz Title",
                          "questions": [
                            {
                              "id": 1,
                              "question": "What is ...?",
                              "options": ["Option 1", "Option 2", "Option 3", "Option 4"],
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
                            val json = aiResponse.text ?: ""

//                            Log.d("Quizzinghere brother",aiResponse.text.toString()   )


                            if (aiResponse.text?.isNotEmpty()!!) {
                                creatingQuizzs.value = true
                            } else
                                creatingQuizzs.value = false

                            val quiz = parseQuizJson(json)

                            withContext(Dispatchers.Main) {
                                _quizState.value = QuizState(
                                    quiz = quiz,
                                    isLoading = false
                                )
                            }

                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                _quizState.value = QuizState(
                                    errorMessage = "Failed to generate quiz: ${e.message}",
                                    isLoading = false
                                )

                                creatingQuizzs.value = false

                            }

                            creatingQuizzs.value = false

                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            _quizState.value = QuizState(
                                errorMessage = "No text found in PDF.",
                                isLoading = false
                            )
                        }
                    }

                }
            } catch (e: Exception) {
                extractingTextFromPdfQuizz.value = "Failed to extract text: ${e.message}"

                extractingFromPdfQuizSucessfull.value = false

                creatingQuizzs.value = false

                withContext(Dispatchers.Main) {
                    _quizState.value = QuizState(
                        errorMessage = "Failed to extract text: ${e.message}",
                        isLoading = false
                    )
                }

            }
        }
    }


//    private fun parseQuizJson(json: String): Quiz? {
//        return try {
//            val jsonObj = JSONObject(json)
//            val title = jsonObj.optString("title", "Generated Quiz")
//            val questionsArray = jsonObj.getJSONArray("questions")
//
//            val questionsList = mutableListOf<Question>()
//            for (i in 0 until questionsArray.length()) {
//                val qObj = questionsArray.getJSONObject(i)
//                val id = qObj.getInt("id")
//                val question = qObj.getString("question")
//                val optionsArray = qObj.getJSONArray("options")
//                val options = List(optionsArray.length()) { idx -> optionsArray.getString(idx) }
//                val correctAnswer = qObj.getInt("correctAnswer")
//                val explanation = qObj.optString("explanation", null)
//
//                questionsList.add(
//                    Question(
//                        id = id,
//                        question = question,
//                        options = options,
//                        correctAnswer = correctAnswer,
//                        explanation = explanation
//                    )
//                )
//            }
//
//            Quiz(title = title, questions = questionsList)
//        } catch (e: Exception) {
//            null
//        }
//    }


    private fun parseQuizJson(rawResponse: String): Quiz? {
        return try {
            // Step 1: Extract only the JSON part (remove ```json and ``` etc.)
            val json = rawResponse
                .substringAfter("{")    // Take from the first '{'
                .substringBeforeLast("}") + "}" // Take until the last '}'

            // Step 2: Parse the clean JSON
            val jsonObj = JSONObject(json)
            val title = jsonObj.optString("title", "Generated Quiz")
            val questionsArray = jsonObj.getJSONArray("questions")

            val questionsList = mutableListOf<Question>()
            for (i in 0 until questionsArray.length()) {
                val qObj = questionsArray.getJSONObject(i)
                val id = qObj.optInt("id", i + 1) // fallback to index
                val question = qObj.optString("question", "No question provided")
                val optionsArray = qObj.optJSONArray("options") ?: JSONArray()
                val options = List(optionsArray.length()) { idx ->
                    optionsArray.optString(
                        idx,
                        "Option ${idx + 1}"
                    )
                }
                val correctAnswer = qObj.optInt("correctAnswer", 0)
                val explanation = qObj.optString("explanation", "No explanation provided")

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

            Quiz(title = title, questions = questionsList)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
//
//    private val _quizzState = MutableStateFlow<Quiz?>(null)
//    val quizzStatem: StateFlow<Quiz?> = _quizzState
//
//    fun setQuizzFromResponse(response: String) {
//        _quizzState.value = parseQuizJson(response)
//    }
//    fun checkAnswer(questionId: Int, selectedOption: Int) {
//        // You can log, save, or analyze user's answer here
//        Log.d("Quiz", "Question $questionId selected option: $selectedOption")
//    }

    private val _peopleList = MutableStateFlow<List<Person>>(emptyList())
    val peopleList: StateFlow<List<Person>> = _peopleList

    private val _errorQuizz = MutableStateFlow<String?>(null)
    val errorQizz: StateFlow<String?> = _errorQuizz

    fun loadPeopleFromJson(rawJson: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cleaned = sanitizeJson(rawJson)
                val people = mutableListOf<Person>()

                if (cleaned.trimStart().startsWith("[")) {
                    parseArrayToPeople(JSONArray(cleaned), people)
                } else if (cleaned.trimStart().startsWith("{")) {
                    val obj = JSONObject(cleaned)
                    val arr = when {
                        obj.has("people") && obj.opt("people") is JSONArray -> obj.getJSONArray("people")
                        obj.has("data") && obj.opt("data") is JSONArray -> obj.getJSONArray("data")
                        else -> JSONArray().put(obj)
                    }
                    parseArrayToPeople(arr, people)
                }

                _peopleList.value = people
                _errorQuizz.value = null
            } catch (t: Throwable) {
                _peopleList.value = emptyList()
                _errorQuizz.value = "Parsing error: ${t.message}"
            }
        }
    }

    private fun parseArrayToPeople(arr: JSONArray, outList: MutableList<Person>) {
        for (i in 0 until arr.length()) {
            try {
                val obj = arr.optJSONObject(i) ?: JSONObject(arr.get(i).toString())
                val id = obj.optInt("id", -1)
                val name = obj.optString("name", "Unknown")
                val text = obj.optString("text", "")
                val age = obj.optInt("age", 0)

                outList.add(Person(id = id, name = name, text = text, age = age))
            } catch (e: Exception) {
                Log.w("PeopleViewModel", "Skipping element $i due to parse error: ${e.message}")
            }
        }
    }

    private fun sanitizeJson(raw: String): String {
        var s = raw.trim()
        s = s.replace("```json", "", ignoreCase = true)
            .replace("```", "")
            .trim()

        val first = s.indexOfFirst { it == '{' || it == '[' }
        val last = s.indexOfLast { it == '}' || it == ']' }

        return if (first >= 0 && last >= 0 && last >= first) {
            s.substring(first, last + 1).trim()
        } else s
    }


}


class Person(
    val id: Int,
    val name: String,
    val text: String,
    val age: Int
)