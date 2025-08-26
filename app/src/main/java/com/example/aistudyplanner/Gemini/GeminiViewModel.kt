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

        try {

            viewModelScope.launch {
                val aiResponse =
                    model.generateContent("Summarize this YouTube video in a clear and concise manner: [${url}]. Include the main points, key insights, and any actionable steps or tips provided in the video. Keep the summary under 200 words and structure it with bullet points for clarity")
                youtubeSummaryResponse.value = aiResponse.text.toString()
//                Log.d("GoogleAIFromVIewmodl",)

                if (aiResponse.text.toString().isNotEmpty()) {
                    youtubeSummaryisLoading.value = false
//                    youtubeSummaryError.value = false
                } else {
                    youtubeSummaryisLoading.value = true
//                    youtubeSummaryError.value = true
                }
            }
        } catch (e: Exception) {
            youtubeSummaryError.value = true
            tipLoading.value = true
        }
    }

//
//
//    // YouTube Summarizer with Transcript Extraction
//    val youtubeSummaryResponseC = MutableStateFlow<String>("")
//    val youtubeSummaryisLoadingC = MutableStateFlow<Boolean>(false)
//    val youtubeSummaryErrorC = MutableStateFlow<String>("")
//
//    fun YTSummariesC(url: String) {
//        youtubeSummaryisLoadingC.value = true
//        youtubeSummaryErrorC.value = ""
//
//        viewModelScope.launch {
//            try {
//                // Step 1: Extract video ID from URL
//                val videoId = extractVideoId(url)
//                if (videoId == null) {
//                    youtubeSummaryErrorC.value = "Invalid YouTube URL"
//                    youtubeSummaryisLoadingC.value = false
//                    return@launch
//                }
//
//                // Step 2: Get transcript
//                val transcript = getYouTubeTranscript(videoId)
//                if (transcript.isNullOrEmpty()) {
//                    youtubeSummaryErrorC.value = "Could not fetch transcript. Video might not have captions or subtitles available."
//                    youtubeSummaryisLoadingC.value = false
//                    return@launch
//                }
//
//                // Step 3: Summarize with Gemini using actual transcript
//                val prompt = """
//                    Please provide a comprehensive summary of the following YouTube video content.
//                    Focus on the main points, key insights, and important information.
//                    Structure it with bullet points for clarity and keep it under 200 words.
//
//                    Video Content:
//                    $transcript
//
//                    Summary:
//                """.trimIndent()
//
//                val aiResponse = model.generateContent(prompt)
//                val summary = aiResponse.text?.toString()
//
//                if (!summary.isNullOrEmpty()) {
//                    youtubeSummaryResponseC.value = summary
//                    youtubeSummaryisLoadingC.value = false
//                } else {
//                    youtubeSummaryErrorC.value = "Unable to generate summary"
//                    youtubeSummaryisLoadingC.value = false
//                }
//
//            } catch (e: Exception) {
//                Log.e("YTSummary", "Error: ${e.message}", e)
//                youtubeSummaryErrorC.value = "Error: ${e.message}"
//                youtubeSummaryisLoadingC.value = false
//            }
//        }
//    }
//
//    /**
//     * Extract YouTube video ID from various URL formats
//     */
//    private fun extractVideoId(url: String): String? {
//        val patterns = listOf(
//            "(?:youtube\\.com\\/watch\\?v=|youtu\\.be\\/|youtube\\.com\\/embed\\/)([^&\\n?#]+)",
//            "youtube\\.com\\/watch\\?.*v=([^&\\n?#]+)"
//        )
//
//        for (pattern in patterns) {
//            val matcher = Pattern.compile(pattern).matcher(url)
//            if (matcher.find()) {
//                return matcher.group(1)
//            }
//        }
//        return null
//    }
//
//    /**
//     * Get transcript from YouTube video
//     */
//    private suspend fun getYouTubeTranscript(videoId: String): String? {
//        return withContext(Dispatchers.IO) {
//            try {
//                // Try to get transcript from YouTube's internal API
//                getTranscriptFromInternalAPI(videoId)
//                    ?: getTranscriptFromHTML(videoId) // Fallback method
//
//            } catch (e: Exception) {
//                Log.e("Transcript", "Error getting transcript: ${e.message}")
//                null
//            }
//        }
//    }
//
//    /**
//     * Method 1: Get transcript using YouTube's internal transcript API
//     */
//    private suspend fun getTranscriptFromInternalAPI(videoId: String): String? {
//        return try {
//            // Get video page to extract transcript URLs
//            val videoPageUrl = "https://www.youtube.com/watch?v=$videoId"
//            val request = DownloadManager.Request.Builder()
//                .url(videoPageUrl)
//                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
//                .build()
//
//            val response = client.newCall(request).execute()
//            val html = response.body?.string() ?: return null
//
//            // Extract transcript URL from the page
//            val transcriptRegex = "\"captionTracks\":\\[([^\\]]+)\\]"
//            val matcher = Pattern.compile(transcriptRegex).matcher(html)
//
//            if (matcher.find()) {
//                val captionTracksJson = "[${matcher.group(1)}]"
//                val captionTracks = JSONArray(captionTracksJson)
//
//                // Find English transcript or first available
//                var transcriptUrl: String? = null
//                for (i in 0 until captionTracks.length()) {
//                    val track = captionTracks.getJSONObject(i)
//                    if (track.has("baseUrl")) {
//                        transcriptUrl = track.getString("baseUrl")
//                        val languageCode = track.optJSONObject("languageCode")?.optString("simpleText") ?: ""
//                        if (languageCode.contains("en", true)) {
//                            break // Prefer English
//                        }
//                    }
//                }
//
//                if (transcriptUrl != null) {
//                    return downloadTranscriptXML(transcriptUrl)
//                }
//            }
//
//            null
//        } catch (e: Exception) {
//            Log.e("Transcript", "Internal API method failed: ${e.message}")
//            null
//        }
//    }
//
//    /**
//     * Method 2: Fallback - Extract from video page HTML
//     */
//    private suspend fun getTranscriptFromHTML(videoId: String): String? {
//        return try {
//            val videoUrl = "https://www.youtube.com/watch?v=$videoId"
//            val request = Request.Builder()
//                .url(videoUrl)
//                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
//                .build()
//
//            val response = client.newCall(request).execute()
//            val html = response.body?.string() ?: return null
//
//            // Look for automatic captions in the page data
//            val scriptRegex = "var ytInitialPlayerResponse = (\\{.+?\\});"
//            val matcher = Pattern.compile(scriptRegex).matcher(html)
//
//            if (matcher.find()) {
//                val playerResponse = JSONObject(matcher.group(1))
//
//                // Navigate through JSON to find captions
//                val captions = playerResponse
//                    .optJSONObject("captions")
//                    ?.optJSONObject("playerCaptionsTracklistRenderer")
//                    ?.optJSONArray("captionTracks")
//
//                if (captions != null && captions.length() > 0) {
//                    val captionTrack = captions.getJSONObject(0)
//                    val baseUrl = captionTrack.getString("baseUrl")
//                    return downloadTranscriptXML(baseUrl)
//                }
//            }
//
//            null
//        } catch (e: Exception) {
//            Log.e("Transcript", "HTML method failed: ${e.message}")
//            null
//        }
//    }
//
//    /**
//     * Download and parse transcript XML
//     */
//    private suspend fun downloadTranscriptXML(url: String): String? {
//        return try {
//            val request = Request.Builder()
//                .url(url)
//                .build()
//
//            val response = client.newCall(request).execute()
//            val xmlContent = response.body?.string() ?: return null
//
//            // Parse XML and extract text
//            val doc = Jsoup.parse(xmlContent)
//            val textElements = doc.select("text")
//
//            val transcript = StringBuilder()
//            for (element in textElements) {
//                val text = element.text()
//                if (text.isNotBlank()) {
//                    transcript.append(text).append(" ")
//                }
//            }
//
//            val cleanedTranscript = transcript.toString().trim()
//            Log.d("Transcript", "Extracted transcript length: ${cleanedTranscript.length}")
//
//            cleanedTranscript
//
//        } catch (e: Exception) {
//            Log.e("Transcript", "XML download failed: ${e.message}")
//            null
//        }
//    }
}


