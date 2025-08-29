package com.example.aistudyplanner.pdfExtaction

import android.net.Uri
import android.text.Selection
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp


@Composable
fun PdfFilePicker(onPdfSelection: (Uri) -> Unit,pdfModifier: Modifier = Modifier) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),

    ) {
        uri: Uri? ->
        uri?.let {
            onPdfSelection(it)
        }
    }

    Button(
        onClick = {
            launcher.launch(arrayOf("application/pdf")) // only pdf files
        },
        modifier = pdfModifier
            .padding(16.dp)
    ) {
        Text("SelectPdf")
    }



}