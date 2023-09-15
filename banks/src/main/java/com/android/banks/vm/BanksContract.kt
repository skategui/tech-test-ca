package com.android.banks.vm

import com.android.common.base.UiSingleEvent
import com.android.common.base.UiState
import com.android.common.base.UserInteraction
import com.android.common.model.Banks


class BanksContract {

    // Events that user performed
    sealed class Interaction : UserInteraction {
        data class AccountClicked(val accountId: String) : Interaction()
    }

    // current UI State
    data class State(
        val isLoading: Boolean = false,
        val caBanks: List<Banks> = emptyList(),
        val otherBanks: List<Banks> = emptyList(),
    ) : UiState

    // single event to be made on the screen
    sealed class SingleEvent : UiSingleEvent {
        data class GoAccountDetailScreen(val accountId: String) : SingleEvent()
        object DisplayErrorMessage : SingleEvent()
    }
}