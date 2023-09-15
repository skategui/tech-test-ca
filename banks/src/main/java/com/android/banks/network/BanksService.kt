package com.android.banks.network

import retrofit2.Response
import retrofit2.http.GET

/**
 * Service associated to the request to get the banks list. This is used by Retrofit
 */
interface BanksService {

    @GET("banks.json")
    suspend fun banks(): Response<List<BanksResponse>>
}