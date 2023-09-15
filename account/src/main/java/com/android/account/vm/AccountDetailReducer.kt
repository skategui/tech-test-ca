package com.android.account.vm

import com.android.common.model.Operation

class AccountDetailReducer {

    fun reduce(
        state: AccountDetailContract.State, partialState: PartialState
    ): AccountDetailContract.State {
        return when (partialState) {
            is PartialState.DisplayLoader -> state.copy(
                isLoading = true,
            )

            PartialState.HideLoader -> state.copy(
                isLoading = false,
            )

            is PartialState.RenderAccount -> state.copy(
                balance = partialState.balance,
                operations = partialState.operations,
                title = partialState.label,
                isLoading = false
            )
        }
    }

    sealed class PartialState {
        object DisplayLoader : PartialState()
        object HideLoader : PartialState()
        data class RenderAccount(
            val balance: Float, val label: String, val operations: List<Operation>
        ) : PartialState()
    }
}