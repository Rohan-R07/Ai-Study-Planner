package com.example.aistudyplanner.OnBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DotIndicator(pagerState: PagerState, pageCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        repeat(pageCount) { index ->
            val isSelected = pagerState.currentPage == index
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (isSelected) 12.dp else 8.dp)
                    .background(
                        if (isSelected) Color.Black else Color.Gray, shape = CircleShape
                    )
            )
        }
    }

}

@Preview
@Composable
private fun DNEFOEEF() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val pageCount = 3
    DotIndicator(pagerState, pageCount)
}