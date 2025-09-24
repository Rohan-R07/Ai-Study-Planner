package com.example.aistudyplanner.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.use

fun getPdfFileName(context: Context, uri: Uri): String {
    val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    firebaseCrashlytics.log("helper function in order to get pdf name and preview")
    return try {

        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex) ?: "Unknown PDF"
        } ?: "Unknown PDF"
    } catch (e: Exception) {
        "Unknown PDF"
    }
}


// Helper function to generate PDF preview
suspend fun generatePdfPreview(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                PdfRenderer(pfd).use { renderer ->
                    if (renderer.pageCount > 0) {
                        renderer.openPage(0).use { page ->
                            val bitmap = Bitmap.createBitmap(
                                page.width, page.height, Bitmap.Config.ARGB_8888
                            )
                            page.render(
                                bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )
                            bitmap
                        }
                    } else null
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}