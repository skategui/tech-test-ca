package com.android.banks.vm

import com.android.common.model.Banks

class BanksReducer {

    fun reduce(
        state: BanksContract.State, partialState: PartialState
    ): BanksContract.State {
        return when (partialState) {
            is PartialState.DisplayLoader -> state.copy(
                isLoading = true,
            )

            PartialState.HideLoader -> state.copy(
                isLoading = false,
            )

            is PartialState.RenderBanks -> state.copy(
                caBanks = partialState.caBanks,
                otherBanks = partialState.otherBanks,
                isLoading = false
            )
        }
    }

    sealed class PartialState {
        object DisplayLoader : PartialState()
        object HideLoader : PartialState()
        data class RenderBanks(val caBanks: List<Banks>, val otherBanks: List<Banks>) :
            PartialState()
    }
}