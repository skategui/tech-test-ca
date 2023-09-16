package com.android.banks.vm

import androidx.lifecycle.viewModelScope
import com.android.banks.usecase.GetBanksUsecase
import com.android.common.base.BaseViewModel
import com.android.common.model.Banks
import com.android.common.tracker.Tracker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  This VM is doing the interface between the model and the list of banks on the main screen
 */
class BanksViewModel(
    private val banksUsecase: GetBanksUsecase,
    private val tracker : Tracker,
    private val reducer: BanksReducer = BanksReducer(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<BanksContract.Interaction, BanksContract.State, BanksContract.SingleEvent>(
    BanksContract.State()
) {

    init {
        loadRemoteBanks()
        subscribeBanks()
    }

    /**
     *  Load the data from the remote server
     */
    private fun loadRemoteBanks() {
        viewModelScope.launch(ioDispatcher) {
            try {
                setState { reducer.reduce(this, BanksReducer.PartialState.DisplayLoader) }
                banksUsecase.loadBanks()
                setState { reducer.reduce(this, BanksReducer.PartialState.HideLoader) }
            } catch (e: Exception) {
                handleErrors(e)
            }
        }
    }

    /**
     *  Listen to new banks info to update the UI accordingly
     */
    private fun subscribeBanks() {
        viewModelScope.launch(ioDispatcher) {
            banksUsecase.banks().collect { banks ->
                renderBanks(banks)
            }
        }
    }

    /**
     * Emit a new state with the loaded banks to render on the UI
     */
    private fun renderBanks(banks: List<Banks>) {

        val caBanks = banks.filter { it.isCA }.sortedBy { it.name }
        val otherBanks = banks.filter { !it.isCA }.sortedBy { it.name }

        setState {
            reducer.reduce(
                this,
                BanksReducer.PartialState.RenderBanks(caBanks = caBanks, otherBanks = otherBanks)
            )
        }
    }

    /**
     * Event received from the user interaction, from the UI
     */
    override fun subscribeUserInteraction(event: BanksContract.Interaction) {
        when (event) {
            is BanksContract.Interaction.AccountClicked -> goToAccountDetailScreen(event.accountId)
        }
    }

    /**
     * Emit an event to redirect the ui to the Account detail screen
     */
    private fun goToAccountDetailScreen(bankId: String) {
        setSingleEvent { BanksContract.SingleEvent.GoAccountDetailScreen(bankId) }
    }

    /**
     * Where the errors should be handled. For the sake of this Test, I simplified it
     */
    private fun handleErrors(throwable: Throwable?) {
        // Log error to the server with the user info / context
        tracker.error(this::class.toString(), throwable?.message)
        // Check if the different type of exception that could be thrown to display a specific message to the user
        setState { reducer.reduce(this, BanksReducer.PartialState.HideLoader) }
        setSingleEvent { BanksContract.SingleEvent.DisplayErrorMessage }
    }
}