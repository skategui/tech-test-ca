package com.android.banks.datastore

import com.android.common.datastore.BanksDatastore
import com.android.common.model.Banks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// for the sake of this test, the data will be stored in the memory.
// In the real life, I would use ROOM in order to store the data in the local DB
internal class BanksDatastoreImpl : BanksDatastore {
    private val banksStored: MutableStateFlow<List<Banks>> = MutableStateFlow(emptyList())

    /**
     * Store the list of banks in memory
     */
    override suspend fun storeBanks(banks: List<Banks>) {
        banksStored.value = banks
    }

    // will emit the latest version, making the app reactive
    override suspend fun banks() = banksStored.asStateFlow()
}

