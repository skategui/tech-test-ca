package com.android.banks.repository

import com.android.banks.network.AccountDetailResponse
import com.android.banks.network.BanksResponse
import com.android.banks.network.BanksService
import com.android.banks.network.OperationResponse
import com.android.banks.network.toBanks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

internal class BanksRepositoryTest {

    @MockK
    private lateinit var service: BanksService

    private lateinit var repo: BanksRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repo = BanksRepositoryImpl(service)
    }


    @Test
    fun `Should return the list of banks`() = runTest {

        val banksResponse = listOf(
            BanksResponse(
                isCA = 1, name = "Boursorama", accounts = listOf(
                    AccountDetailResponse(
                        balance = 100.23f,
                        id = "id",
                        label = "label",
                        operations = listOf(
                            OperationResponse(
                                amount = "100", timestamp = "1644611558", title = "operationTitle"
                            ), OperationResponse(
                                amount = "200", timestamp = "1644611558", title = "operationTitle2"
                            )
                        ),
                    )
                )
            )
        )

        coEvery { service.banks() } returns Response.success(banksResponse)
        Assert.assertEquals(banksResponse.map { it.toBanks() }, repo.banks())
    }
}