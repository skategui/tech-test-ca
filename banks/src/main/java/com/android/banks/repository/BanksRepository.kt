package com.android.banks.repository

import com.android.banks.network.BanksService
import com.android.banks.network.toBanks
import com.android.common.model.Banks

/**
 * BanksRepositoryImpl is responsible to make the HTTP request to the server in order to load the list of banks
 * (using retrofit)
 */
internal class BanksRepositoryImpl(private val service: BanksService) : BanksRepository {

    /**
     * Get the list of banks  from the server.
     *  @return  [List] [Banks] Response from the server, containing the list of banks if success
     */
    override suspend fun banks(): List<Banks> = service.banks().body()!!
        .map { response -> response.toBanks() } // map to the entity I will use in the app
}

/**
 * BanksRepository is is an interface to hide the implementation of the repository.
 * PS : it's a bit over engineered for this interface, but useful for the unit tests :)
 */
interface BanksRepository {
    suspend fun banks(): List<Banks>
}