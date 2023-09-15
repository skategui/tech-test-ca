package com.android.account.usecase

import app.cash.turbine.test
import com.android.common.datastore.BanksDatastore
import com.android.common.model.AccountDetail
import com.android.common.model.Banks
import com.android.common.model.Operation
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAccountUsecaseTest {

    @RelaxedMockK
    private lateinit var datastore: BanksDatastore

    @MockK
    private lateinit var usecase: GetAccountUsecase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        usecase = GetAccountUsecaseImpl(datastore)
    }

    @After
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `When getting an account with a valid accountId it gives the associated accountDetail`() = runTest {

        val accountId = "12"
        val accountDetail = AccountDetail(
            balance = 100.23f,
            id = accountId,
            title = "label",
            operations = listOf(
                Operation(
                    amount = "100", timestamp = 1644611558, title = "operationTitle"
                ), Operation(
                    amount = "200", timestamp = 1644611558, title = "operationTitle2"
                )
            ),
        )
        val banks = listOf(Banks(isCA = true, name = "Boursorama", accounts = listOf(accountDetail)))

        val flow = MutableSharedFlow<List<Banks>>()
        coEvery { datastore.banks() } returns flow
        usecase.getAccountDetails(accountId).test {
            flow.emit(banks)
            coVerify { datastore.banks() }
            Assert.assertEquals(accountDetail, awaitItem())
         }
    }

    @Test
    fun `When getting an account with a invalid accountId it gives a null object`() = runTest {

        val accountId = "12"
        val accountDetail = AccountDetail(
            balance = 100.23f,
            id = accountId,
            title = "label",
            operations = listOf(
                Operation(
                    amount = "100", timestamp = 1644611558, title = "operationTitle"
                ), Operation(
                    amount = "200", timestamp = 1644611558, title = "operationTitle2"
                )
            ),
        )
        val banks = listOf(Banks(isCA = true, name = "Boursorama", accounts = listOf(accountDetail)))

        val flow = MutableSharedFlow<List<Banks>>()
        coEvery { datastore.banks() } returns flow

        usecase.getAccountDetails("not_exiting_id").test {
            flow.emit(banks)
            coVerify { datastore.banks() }
            Assert.assertNull(awaitItem())
        }
    }

}