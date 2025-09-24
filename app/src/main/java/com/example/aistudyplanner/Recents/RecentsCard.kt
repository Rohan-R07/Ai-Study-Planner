package com.example.aistudyplanner.Recents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aistudyplanner.R
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Composable
fun RecentsCard(
    recentFile: RecentsPdf,
    onFileClick: (RecentsPdf) -> Unit = {},
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.pdf_image
) {

    val firebaseCrashlytics = FirebaseCrashlytics.getInstance()
    // Individual Recent File Card

    Card(
        modifier = modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .height(90.dp)
            .clickable {
                firebaseCrashlytics.log("Recents card onCLick from home screen")
                onFileClick(recentFile)
            },
        colors = CardDefaults.cardColors(
//            containerColor =  // Dark card background
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // PDF Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        Color(0xFFE53E3E).copy(alpha = 0.2f),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon), // Replace with your PDF icon resource
                    contentDescription = "PDF File",
                    tint = Color(0xFFE53E3E),
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // File Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // File Name
                Text(
                    text = recentFile.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

            }
        }
    }


}

