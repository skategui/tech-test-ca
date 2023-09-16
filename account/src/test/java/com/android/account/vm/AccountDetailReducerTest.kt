package com.android.account.vm

import com.android.common.model.Operation
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Date

internal class AccountDetailReducerTest {

    private lateinit var reducer: AccountDetailReducer

    @Before
    fun setup() {
        reducer = AccountDetailReducer()
    }

    @Test
    fun `State has isLoading=true when receiving DisplayLoader PartialState`() {

        val currentState = AccountDetailContract.State(
            isLoading = false
        )
        val response = reducer.reduce(currentState, AccountDetailReducer.PartialState.DisplayLoader)
        Assert.assertEquals(
            currentState.copy(
                isLoading = true,
            ), response
        )
    }

    @Test
    fun `State has isLoading=false when receiving HideLoader PartialState`() {

        val currentState = AccountDetailContract.State(
            isLoading = true
        )

        val response = reducer.reduce(currentState, AccountDetailReducer.PartialState.HideLoader)
        Assert.assertEquals(currentState.copy(isLoading = false), response)
    }

    @Test
    fun `State has both list of banks when receiving RenderAccount PartialState`() {

        val balance = 120f
        val label = "label"
        val operations = listOf(
            Operation(
                amount = 100f, timestamp = Date().time, title = "operationTitle"
            ), Operation(
                amount = 200f, timestamp = Date().time, title = "operationTitle2"
            )
        )
        val currentState = AccountDetailContract.State(
            isLoading = true, title = "toto", balance = 1f
        )

        val response = reducer.reduce(
            currentState,
            AccountDetailReducer.PartialState.RenderAccount(balance, label, operations)
        )
        Assert.assertEquals(
            currentState.copy(
                isLoading = false, title = label, operations = operations, balance = balance
            ), response
        )
    }
}