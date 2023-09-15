package com.android.common.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.common.ui.theme.BackgroundColor

// My own divider, to avoid duplicating the component
@Composable
fun CADivider(modifier: Modifier, paddingStart: Dp) {
    androidx.compose.material3.Divider(
        modifier
            .fillMaxWidth()
            .padding(start = paddingStart),
        color = BackgroundColor,
        thickness = 1.dp
    )
}
