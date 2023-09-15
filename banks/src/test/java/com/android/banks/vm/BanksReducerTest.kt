package com.android.banks.vm

import com.android.common.model.AccountDetail
import com.android.common.model.Banks
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class BanksReducerTest {

    private lateinit var reducer: BanksReducer

    private val caBanks = listOf(
        Banks(
            isCA = true, name = "Boursorama", accounts = listOf(
                AccountDetail(
                    balance = 100.23f,
                    id = "id",
                    title = "label",
                    operations = listOf(),
                )
            )
        )
    )

    private val otherBanks = listOf(
        Banks(
            isCA = true, name = "Qonto", accounts = listOf(
                AccountDetail(
                    balance = 200f,
                    id = "id2",
                    title = "label2",
                    operations = listOf(),
                ), AccountDetail(
                    balance = 300.50f,
                    id = "id3",
                    title = "label3",
                    operations = listOf(),
                )
            )
        )
    )

    @Before
    fun setup() {
        reducer = BanksReducer()
    }

    @Test
    fun `State has isLoading=true when receiving DisplayLoader PartialState`() {

        val currentState = BanksContract.State(
            isLoading = true, caBanks = emptyList(), otherBanks = emptyList()
        )
        val response =
            reducer.reduce(currentState, BanksReducer.PartialState.RenderBanks(caBanks, otherBanks))
        Assert.assertEquals(
            currentState.copy(
                isLoading = false,
                caBanks = caBanks,
                otherBanks = otherBanks
            ), response
        )
    }

    @Test
    fun `State has isLoading=false when receiving HideLoader PartialState`() {

        val currentState = BanksContract.State(
            isLoading = true, caBanks = emptyList(), otherBanks = emptyList()
        )

        val response = reducer.reduce(currentState, BanksReducer.PartialState.HideLoader)
        Assert.assertEquals(currentState.copy(isLoading = false), response)
    }

    @Test
    fun `State has both list of banks when receiving RenderBanks PartialState`() {

        val currentState = BanksContract.State(
            isLoading = true, caBanks = emptyList(), otherBanks = emptyList()
        )

        val response = reducer.reduce(currentState, BanksReducer.PartialState.HideLoader)
        Assert.assertEquals(currentState.copy(isLoading = false), response)
    }
}