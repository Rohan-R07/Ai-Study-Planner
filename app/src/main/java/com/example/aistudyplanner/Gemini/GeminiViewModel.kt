package com.example.aistudyplanner.Gemini

import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.IOException
import java.util.regex.Pattern
import java.util.regex.Pattern.*
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withContext

class GeminiViewModel : ViewModel() {

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

//     YouTube Summarizer with Transcript Extraction
//     HTTP client for transcript fetching
//     YouTube Summary variables



//    private val httpClient = OkHttpClient.Builder()
//        .followRedirects(true)
//        .followSslRedirects(true)
//        .build()
//    val youtubeSummaryResponse = MutableStateFlow<String>("")
//    val youtubeSummaryisLoading = MutableStateFlow<Boolean>(false)
//    val youtubeSummaryError = MutableStateFlow<String>("")
//
//    fun YTSummaries(url: String) {
//        youtubeSummaryisLoading.value = true
//        youtubeSummaryError.value = ""
//        youtubeSummaryResponse.value = ""
//
//        viewModelScope.launch {
//            try {
//                Log.d("YouTubeTranscript", "Starting transcript extraction for: $url")
//
//                // Step 1: Extract video ID
//                val videoId = extractVideoId(url)
//                if (videoId == null) {
//                    youtubeSummaryError.value = "Invalid YouTube URL"
//                    youtubeSummaryisLoading.value = false
//                    return@launch
//                }
//
//                Log.d("YouTubeTranscript", "Extracted video ID: $videoId")
//
//                // Step 2: Get actual transcript
//                val transcript = getYouTubeTranscript(videoId)
//                if (transcript.isNullOrBlank()) {
//                    youtubeSummaryError.value = "Could not fetch transcript. Video might not have captions or subtitles."
//                    youtubeSummaryisLoading.value = false
//                    return@launch
//                }
//
//                Log.d("YouTubeTranscript", "Got transcript length: ${transcript.length}")
//                Log.d("YouTubeTranscript", "Transcript preview: ${transcript.take(200)}...")
//
//                // Step 3: Summarize with Gemini
//                val prompt = """
//                    Please provide a comprehensive summary of this YouTube video based on its transcript.
//                    Focus on the main points, key insights, and important information.
//                    Structure it with bullet points and keep it under 200 words.
//
//                    Video Transcript:
//                    $transcript
//
//                    Summary:
//                """.trimIndent()
//
//                val aiResponse = model.generateContent(prompt)
//                val summary = aiResponse.text ?: "Unable to generate summary"
//
//                youtubeSummaryResponse.value = summary
//                youtubeSummaryisLoading.value = false
//
//            } catch (e: Exception) {
//                Log.e("YouTubeTranscript", "Error in YTSummaries: ${e.message}", e)
//                youtubeSummaryError.value = "Error: ${e.message}"
//                youtubeSummaryisLoading.value = false
//            }
//        }
//    }
//
//    private fun extractVideoId(url: String): String? {
//        val patterns = listOf(
//            "(?:youtube\\.com\\/watch\\?v=)([^&\\n?#]+)",
//            "(?:youtu\\.be\\/)([^&\\n?#]+)",
//            "(?:youtube\\.com\\/embed\\/)([^&\\n?#]+)",
//            "(?:youtube\\.com\\/v\\/)([^&\\n?#]+)"
//        )
//
//        for (pattern in patterns) {
//            val matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(url)
//            if (matcher.find()) {
//                return matcher.group(1)?.take(11) // YouTube video IDs are 11 characters
//            }
//        }
//        return null
//    }
//
//    private suspend fun getYouTubeTranscript(videoId: String): String? {
//        return withContext(Dispatchers.IO) {
//            try {
//                Log.d("YouTubeTranscript", "Fetching page for video ID: $videoId")
//
//                // Get video page
//                val videoUrl = "https://www.youtube.com/watch?v=$videoId"
//                val request = Request.Builder()
//                    .url(videoUrl)
//                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
//                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                    .addHeader("Accept-Language", "en-US,en;q=0.5")
//                    .build()
//
//                val response = httpClient.newCall(request).execute()
//
//                if (!response.isSuccessful) {
//                    Log.e("YouTubeTranscript", "Failed to fetch video page: ${response.code}")
//                    return@withContext null
//                }
//
//                val html = response.body?.string()
//                if (html.isNullOrBlank()) {
//                    Log.e("YouTubeTranscript", "Empty HTML response")
//                    return@withContext null
//                }
//
//                Log.d("YouTubeTranscript", "Got HTML, length: ${html.length}")
//
//                // Method 1: Try captionTracks
//                var transcript = extractFromCaptionTracks(html)
//                if (!transcript.isNullOrBlank()) {
//                    Log.d("YouTubeTranscript", "Successfully extracted from captionTracks")
//                    return@withContext transcript
//                }
//
//                // Method 2: Try playerCaptionsTracklistRenderer
//                transcript = extractFromPlayerCaptions(html)
//                if (!transcript.isNullOrBlank()) {
//                    Log.d("YouTubeTranscript", "Successfully extracted from playerCaptions")
//                    return@withContext transcript
//                }
//
//                Log.e("YouTubeTranscript", "No transcript found in video page")
//                return@withContext null
//
//            } catch (e: Exception) {
//                Log.e("YouTubeTranscript", "Error in getYouTubeTranscript: ${e.message}", e)
//                return@withContext null
//            }
//        }
//    }
//
//    private suspend fun extractFromCaptionTracks(html: String): String? {
//        return try {
//            Log.d("YouTubeTranscript", "Trying captionTracks method")
//
//            // Look for captionTracks in the HTML
//            val captionPattern = "\"captionTracks\":\\s*\\[([^\\]]+)\\]"
//            val captionMatcher = Pattern.compile(captionPattern).matcher(html)
//
//            if (captionMatcher.find()) {
//                val captionTracksJson = "[${captionMatcher.group(1)}]"
//                Log.d("YouTubeTranscript", "Found captionTracks JSON")
//
//                val captionTracks = JSONArray(captionTracksJson)
//
//                // Find the best caption track (prefer English)
//                for (i in 0 until captionTracks.length()) {
//                    val track = captionTracks.getJSONObject(i)
//                    if (track.has("baseUrl")) {
//                        val baseUrl = track.getString("baseUrl")
//                        Log.d("YouTubeTranscript", "Found caption URL: $baseUrl")
//
//                        val transcript = downloadTranscript(baseUrl)
//                        if (!transcript.isNullOrBlank()) {
//                            return transcript
//                        }
//                    }
//                }
//            } else {
//                Log.d("YouTubeTranscript", "No captionTracks found")
//            }
//
//            null
//        } catch (e: Exception) {
//            Log.e("YouTubeTranscript", "Error in extractFromCaptionTracks: ${e.message}")
//            null
//        }
//    }
//
//    private suspend fun extractFromPlayerCaptions(html: String): String? {
//        return try {
//            Log.d("YouTubeTranscript", "Trying playerCaptions method")
//
//            // Look for ytInitialPlayerResponse
//            val playerPattern = "var ytInitialPlayerResponse\\s*=\\s*(\\{.+?\\});\\s*var"
//            val playerMatcher = Pattern.compile(playerPattern, Pattern.DOTALL).matcher(html)
//
//            if (playerMatcher.find()) {
//                val playerJson = playerMatcher.group(1)
//                Log.d("YouTubeTranscript", "Found ytInitialPlayerResponse")
//
//                val playerResponse = JSONObject(playerJson)
//                val captions = playerResponse
//                    .optJSONObject("captions")
//                    ?.optJSONObject("playerCaptionsTracklistRenderer")
//                    ?.optJSONArray("captionTracks")
//
//                if (captions != null && captions.length() > 0) {
//                    val track = captions.getJSONObject(0)
//                    val baseUrl = track.getString("baseUrl")
//                    Log.d("YouTubeTranscript", "Found player caption URL: $baseUrl")
//
//                    return downloadTranscript(baseUrl)
//                }
//            } else {
//                Log.d("YouTubeTranscript", "No ytInitialPlayerResponse found")
//            }
//
//            null
//        } catch (e: Exception) {
//            Log.e("YouTubeTranscript", "Error in extractFromPlayerCaptions: ${e.message}")
//            null
//        }
//    }
//
//    private suspend fun downloadTranscript(url: String): String? {
//        return try {
//            Log.d("YouTubeTranscript", "Downloading transcript from: $url")
//
//            val request = Request.Builder()
//                .url(url)
//                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
//                .build()
//
//            val response = httpClient.newCall(request).execute()
//
//            if (!response.isSuccessful) {
//                Log.e("YouTubeTranscript", "Failed to download transcript: ${response.code}")
//                return null
//            }
//
//            val xmlContent = response.body?.string()
//            if (xmlContent.isNullOrBlank()) {
//                Log.e("YouTubeTranscript", "Empty transcript XML")
//                return null
//            }
//
//            Log.d("YouTubeTranscript", "Downloaded XML length: ${xmlContent.length}")
//
//            // Parse XML using Jsoup
//            val doc = Jsoup.parse(xmlContent, "", org.jsoup.parser.Parser.xmlParser())
//            val textElements = doc.select("text")
//
//            if (textElements.isEmpty()) {
//                Log.e("YouTubeTranscript", "No text elements found in XML")
//                return null
//            }
//
//            val transcript = StringBuilder()
//            for (element in textElements) {
//                val text = element.text().trim()
//                if (text.isNotBlank()) {
//                    // Decode HTML entities
//                    val decodedText = text
//                        .replace("&amp;", "&")
//                        .replace("&lt;", "<")
//                        .replace("&gt;", ">")
//                        .replace("&quot;", "\"")
//                        .replace("&#39;", "'")
//
//                    transcript.append(decodedText).append(" ")
//                }
//            }
//
//            val finalTranscript = transcript.toString().trim()
//            Log.d("YouTubeTranscript", "Final transcript length: ${finalTranscript.length}")
//
//            return if (finalTranscript.isNotBlank()) finalTranscript else null
//
//        } catch (e: Exception) {
//            Log.e("YouTubeTranscript", "Error downloading transcript: ${e.message}", e)
//            null
//        }
//    }


}


