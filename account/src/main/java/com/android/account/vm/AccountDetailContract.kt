package com.android.account.vm

import com.android.common.base.UiSingleEvent
import com.android.common.base.UiState
import com.android.common.base.UserInteraction
import com.android.common.model.Operation


class AccountDetailContract {

    // Events that user performed
    sealed class Interaction : UserInteraction

    // current UI State
    data class State(
        val isLoading: Boolean = false,
        val balance: Float = 0f,
        val title : String = "--",
        val operations : List<Operation> = emptyList(),
    ) : UiState

    // single event to be made on the screen
    sealed class SingleEvent : UiSingleEvent {
        object DisplayErrorMessage : SingleEvent()
    }
}