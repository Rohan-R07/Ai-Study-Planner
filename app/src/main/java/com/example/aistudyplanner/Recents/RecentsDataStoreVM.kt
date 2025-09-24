package com.example.aistudyplanner.Recents

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


// --- DataStore setup ---
private val Context.dataStore by preferencesDataStore("recent_pdfs_pref")
private val KEY_RECENTS = stringPreferencesKey("recent_pdfs_json")
private const val MAX_RECENTS = 20

class RecentsDataStoreVM(app: Application) : AndroidViewModel(app) {

    private val ctx = app.applicationContext

    val recents: StateFlow<List<RecentsPdf>> =
        ctx.dataStore.data.map { prefs ->
            prefs[KEY_RECENTS]?.let { parseJsonList(it) } ?: emptyList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Add or update a PDF in recents
    fun addRecentPdf(uri: Uri) {
        viewModelScope.launch {
            val name = getFileName(ctx, uri)
            val newPdf = RecentsPdf(uri.toString(), name, System.currentTimeMillis())

            ctx.dataStore.edit { prefs ->
                val current = prefs[KEY_RECENTS]?.let { parseJsonList(it).toMutableList() } ?: mutableListOf()
                current.removeAll { it.uri == newPdf.uri }
                current.add(0, newPdf)
                if (current.size > MAX_RECENTS) {
                    current.subList(MAX_RECENTS, current.size).clear()
                }
                prefs[KEY_RECENTS] = listToJson(current)
            }
        }
    }

    fun clearRecents() {
        viewModelScope.launch {
            ctx.dataStore.edit { it.remove(KEY_RECENTS) }
        }
    }





    private fun parseJsonList(jsonStr: String): List<RecentsPdf> {
        val arr = JSONArray(jsonStr)
        val out = mutableListOf<RecentsPdf>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            out.add(
                RecentsPdf(
                    uri = obj.optString("uri"),
                    name = obj.optString("name"),
                    openedAt = obj.optLong("openedAt")
                )
            )
        }
        return out
    }


    private fun listToJson(list: List<RecentsPdf>): String {
        val arr = JSONArray()
        list.forEach { pdf ->
            val obj = JSONObject()
            obj.put("uri", pdf.uri)
            obj.put("name", pdf.name)
            obj.put("openedAt", pdf.openedAt)
            arr.put(obj)
        }
        return arr.toString()
    }


    private fun getFileName(context: Context, uri: Uri): String {
        val cursor = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) it.getString(0) else "unknown.pdf"
        } ?: "unknown.pdf"
    }
}