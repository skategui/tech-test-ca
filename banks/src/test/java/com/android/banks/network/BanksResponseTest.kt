package com.android.banks.network

import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test


internal class BanksResponseTest {

    @Test
    fun `With the right param, I can create a Banks object`() {

        val banksResponse = BanksResponse(
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

        val response = banksResponse.toBanks()
        Assert.assertNotNull(response)
    }

    @Test
    fun `Given an incorrect isCa value, it throw an exception`() = runTest {

        val banksResponse = BanksResponse(
            isCA = 2, name = "Boursorama", accounts = listOf(
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
        Assert.assertThrows(Error::class.java) { banksResponse.toBanks() }
    }

    @Test
    fun `Given an incorrect timestamp, it throw an exception`() = runTest {

        val banksResponse = BanksResponse(
            isCA = 1, name = "Boursorama", accounts = listOf(
                AccountDetailResponse(
                    balance = 100.23f,
                    id = "id",
                    label = "label",
                    operations = listOf(
                        OperationResponse(
                            amount = "100", timestamp = "toto", title = "operationTitle"
                        ), OperationResponse(
                            amount = "200", timestamp = "1644611558", title = "operationTitle2"
                        )
                    ),
                )
            )
        )
        Assert.assertThrows(Error::class.java) { banksResponse.toBanks() }
    }
}