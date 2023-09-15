package com.android.common.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.android.common.ui.theme.LightBlack

// TitleRowItem Composable, used only in the row
@Composable
fun TitleRowItem(modifier: Modifier, title: String) {
    Text(
        text = title,
        color = LightBlack,
        textAlign = TextAlign.Left,
        fontSize = 16.sp,
        modifier = modifier
    )
}