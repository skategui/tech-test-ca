package com.android.banks.usecase

import app.cash.turbine.test
import com.android.banks.repository.BanksRepository
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


internal class GetBanksUsecaseTest {

    @RelaxedMockK
    private lateinit var datastore: BanksDatastore

    @MockK
    private lateinit var repo: BanksRepository

    @MockK
    private lateinit var usecase: GetBanksUsecase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        usecase = GetBanksUsecaseImpl(datastore, repo)
    }

    @After
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `When loading the bank it stores the infos in the datastore`() = runTest {

        val banks = listOf(
            Banks(
                isCA = true, name = "Boursorama", accounts = listOf(
                    AccountDetail(
                        balance = 100.23f,
                        id = "id",
                        title = "label",
                        operations = listOf(
                            Operation(
                                amount = "100", timestamp = 1644611558, title = "operationTitle"
                            ), Operation(
                                amount = "200", timestamp = 1644611558, title = "operationTitle2"
                            )
                        ),
                    )
                )
            )
        )

        coEvery { repo.banks() } returns banks
        usecase.loadBanks()
        coVerify { datastore.storeBanks(banks) }
    }

    @Test
    fun `When a new update is available from the datastore it should emit it`() = runTest {

        val banks = listOf(
            Banks(
                isCA = true, name = "Boursorama", accounts = listOf(
                    AccountDetail(
                        balance = 100.23f,
                        id = "id",
                        title = "label",
                        operations = listOf(
                            Operation(
                                amount = "100", timestamp = 1644611558, title = "operationTitle"
                            ), Operation(
                                amount = "200", timestamp = 1644611558, title = "operationTitle2"
                            )
                        ),
                    )
                )
            )
        )
        val flow = MutableSharedFlow<List<Banks>>()
        coEvery { datastore.banks() } returns flow

        usecase.banks().test {
            flow.emit(banks)
            Assert.assertEquals(banks, awaitItem())
         //   awaitComplete()
        }
    }
}