package com.android.common.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.android.common.ui.theme.MidGrey
import com.android.common.utils.twoDigits

// Pricing Composable, used only in the row
@Composable
fun PricingRowItem(modifier: Modifier, balance: Float) {
    Text(
        text = "${balance.twoDigits()} €",
        color = MidGrey,
        fontSize = 16.sp,
        modifier = modifier
    )
}