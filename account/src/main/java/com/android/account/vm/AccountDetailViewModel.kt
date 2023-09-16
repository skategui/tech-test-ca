package com.android.account.vm

import androidx.lifecycle.viewModelScope
import com.android.account.usecase.GetAccountUsecase
import com.android.common.base.BaseViewModel
import com.android.common.model.AccountDetail
import com.android.common.model.Operation
import com.android.common.tracker.Tracker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  This VM is doing the interface between the model and the account detail screen
 */
class AccountDetailViewModel(
    private val accountId: String?,
    private val tracker : Tracker,
    private val getAccountUsecase: GetAccountUsecase,
    private val reducer: AccountDetailReducer = AccountDetailReducer(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<AccountDetailContract.Interaction, AccountDetailContract.State, AccountDetailContract.SingleEvent>(
    AccountDetailContract.State()
) {

    init {
        if (accountId != null) loadAccount(accountId)
        else displayLoadingAccountError()
    }

    /**
     * Load an account given an existing accountId
     */
    private fun loadAccount(accountId: String) {


        viewModelScope.launch(ioDispatcher) {
            try {
                setState { reducer.reduce(this, AccountDetailReducer.PartialState.DisplayLoader) }
                getAccountUsecase.getAccountDetails(accountId).collect { accountDetail ->
                    if (accountDetail != null) {
                        renderAccount(accountDetail)
                    } else {
                        displayLoadingAccountError()
                    }
                }

            } catch (e: Exception) {
                handleErrors(e)
            }
        }
    }

    /**
     * Emit an event to display an error message on the UI
     */
    private fun displayLoadingAccountError() {
        setSingleEvent { AccountDetailContract.SingleEvent.DisplayErrorMessage }
    }

    /**
     * Emit a new state with the loaded account to render on the UI
     */
    private fun renderAccount(account: AccountDetail) {
        setState {
            reducer.reduce(
                this,
                AccountDetailReducer.PartialState.RenderAccount(
                    balance = account.balance,
                    label = account.title,
                    operations = account.operations.sortedWith(compareByDescending<Operation> { it.timestamp }.thenBy { it.title })
                )
            )
        }
    }

    /**
     * Where the errors should be handled. For the sake of this Test, I simplified it
     */
    private fun handleErrors(throwable: Throwable?) {
        // Log error to the server with the user info / context
        tracker.error(this::class.toString(), throwable?.message)
        // Check if the different type of exception that could be thrown to display a specific message to the user
        setState { reducer.reduce(this, AccountDetailReducer.PartialState.HideLoader) }
        setSingleEvent { AccountDetailContract.SingleEvent.DisplayErrorMessage }
    }
}