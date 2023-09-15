package com.android.account.usecase

import com.android.common.datastore.BanksDatastore
import com.android.common.model.AccountDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * GetAccountUsecaseImpl is responsible to make the get the detail of an account
 */
internal class GetAccountUsecaseImpl(
    private val datastore: BanksDatastore
) : GetAccountUsecase {

    /**
     * Get the detail of an account given an accountId
     * Will return a Flow with the found account, null otherwise
     */
    override suspend fun getAccountDetails(accountId: String): Flow<AccountDetail?> {
        return datastore.banks().map { banks -> banks.flatMap { it.accounts } }
            .map { accounts -> accounts.firstOrNull { it.id == accountId } }
    }

}

// Hide this implementation behind an interface, used in the viewmodel
interface GetAccountUsecase {
    suspend fun getAccountDetails(accountId: String): Flow<AccountDetail?>
}