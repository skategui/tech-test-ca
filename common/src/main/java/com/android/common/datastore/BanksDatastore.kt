package com.android.common.datastore

import com.android.common.model.Banks
import kotlinx.coroutines.flow.Flow

interface BanksDatastore {
    suspend fun storeBanks(banks: List<Banks>)
    suspend fun banks(): Flow<List<Banks>>
}