package com.android.common.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.info.InfoDialog
import com.maxkeppeler.sheets.info.models.InfoBody
import com.maxkeppeler.sheets.info.models.InfoSelection

/**
 * Display a popup for an error message
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorPopup(message: String, closeSelection: () -> Unit) {

    InfoDialog(
        state = rememberUseCaseState(
            visible = true,
            onCloseRequest = { closeSelection() }),
        body = InfoBody.Default(
            bodyText = message,
        ),
        selection = InfoSelection(
            onPositiveClick = {},
            onNegativeClick = {}
        )
    )
}