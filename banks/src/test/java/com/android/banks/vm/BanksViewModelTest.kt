package com.android.banks.vm

import app.cash.turbine.test
import com.android.banks.usecase.GetBanksUsecase
import com.android.common.model.AccountDetail
import com.android.common.model.Banks
import com.android.common.model.Operation
import com.android.common_test.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class BanksViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var vm: BanksViewModel

    @MockK
    private lateinit var usecase: GetBanksUsecase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `When the user clicks on an account, it redirects to the account page`() = runTest {
        val accountId = "12"

        val flow = MutableSharedFlow<List<Banks>>()
        coEvery { usecase.banks() } returns flow
        coEvery { usecase.loadBanks() } returns Unit

        vm = BanksViewModel(banksUsecase = usecase, ioDispatcher = dispatcher)
        vm.setEvent(BanksContract.Interaction.AccountClicked(accountId))

        vm.singleEvent.test {
            val res = awaitItem()
            Assert.assertTrue(res is BanksContract.SingleEvent.GoAccountDetailScreen)
            Assert.assertEquals(
                accountId,
                (res as BanksContract.SingleEvent.GoAccountDetailScreen).accountId
            )
        }
    }

    @Test
    fun `When the vm is initialized, it renders the banks`() = runTest {
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
        coEvery { usecase.banks() } returns flow
        coEvery { usecase.loadBanks() } returns Unit
        vm = BanksViewModel(banksUsecase = usecase, ioDispatcher = dispatcher)

        vm.uiState.test {
            Assert.assertEquals(BanksContract.State(isLoading = false),awaitItem())
            flow.emit(banks)
            Assert.assertEquals(BanksContract.State(isLoading = false, caBanks = banks, otherBanks = emptyList()),awaitItem())
        }
    }

    @Test
    fun `When an error is thrown, it show the error on the UI`() = runTest {

        val flow = MutableSharedFlow<List<Banks>>()
        coEvery { usecase.banks() } returns flow
        coEvery { usecase.loadBanks() }.throws(Exception("error"))
        vm = BanksViewModel(banksUsecase = usecase, ioDispatcher = dispatcher)

        vm.singleEvent.test {
            val res = awaitItem()
            Assert.assertTrue(res is BanksContract.SingleEvent.DisplayErrorMessage)
        }
    }
}