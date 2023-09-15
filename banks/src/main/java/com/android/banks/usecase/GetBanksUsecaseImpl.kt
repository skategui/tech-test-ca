package com.android.banks.usecase

import com.android.banks.repository.BanksRepository
import com.android.common.datastore.BanksDatastore
import com.android.common.model.Banks
import kotlinx.coroutines.flow.Flow

/**
 * GetBanksUsecaseImpl is responsible to get the list of banks
 */
internal class GetBanksUsecaseImpl(
    private val datastore: BanksDatastore,
    private val repository: BanksRepository
) : GetBanksUsecase {

    /**
     * Get the list of banks remotely and store in the repository
     */
    override suspend fun loadBanks() {
        val banks = repository.banks()
        datastore.storeBanks(banks)
    }

    /**
     * Will emit the new banks state everytime it gets updated
     */
    override suspend fun banks() = datastore.banks()
}

// Hide this implementation behind an interface, used in the viewmodel
interface GetBanksUsecase {
    suspend fun loadBanks()
    suspend fun banks(): Flow<List<Banks>>
}