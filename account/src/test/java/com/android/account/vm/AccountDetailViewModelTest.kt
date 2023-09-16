package com.android.account.vm

import app.cash.turbine.test
import com.android.account.usecase.GetAccountUsecase
import com.android.common.model.AccountDetail
import com.android.common.model.Operation
import com.android.common.tracker.Tracker
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


internal class AccountDetailViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var vm: AccountDetailViewModel

    @MockK
    private lateinit var tracker: Tracker

    @MockK
    private lateinit var usecase: GetAccountUsecase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { tracker.error(any(), any()) } returns Unit
        coEvery { tracker.debug(any(), any()) } returns Unit
    }

    @After
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when accountId is valid, it shows the account details`() = runTest {
        val accountId = "12"
        val account = AccountDetail(
            balance = 100.23f,
            id = accountId,
            title = "label",
            operations = listOf(
                Operation(
                    amount = 100f, timestamp = 1644611558, title = "operationTitle"
                ), Operation(
                    amount = 200f, timestamp = 1644611558, title = "operationTitle2"
                )
            ),
        )

        val flow = MutableSharedFlow<AccountDetail?>()
        coEvery { usecase.getAccountDetails(accountId) } returns flow
        vm = AccountDetailViewModel(
            accountId = accountId,
            getAccountUsecase = usecase,
            ioDispatcher = dispatcher,
            tracker = tracker
        )

        vm.uiState.test {
            Assert.assertEquals(AccountDetailContract.State(isLoading = true), awaitItem())
            flow.emit(account)
            Assert.assertEquals(
                AccountDetailContract.State(
                    isLoading = false,
                    balance = account.balance,
                    operations = account.operations,
                    title = account.title
                ), awaitItem()
            )
        }
    }

    @Test
    fun `when accountId is invalid, it shows an error message`() = runTest {
        val accountId = "12"

        val flow = MutableSharedFlow<AccountDetail?>()
        coEvery { usecase.getAccountDetails(accountId) } returns flow
        vm = AccountDetailViewModel(
            accountId = null,
            getAccountUsecase = usecase,
            ioDispatcher = dispatcher,
            tracker = tracker
        )

        vm.uiState.test {
            Assert.assertEquals(AccountDetailContract.State(isLoading = false), awaitItem())
        }

        vm.singleEvent.test {
            val res = awaitItem()
            Assert.assertTrue(res is AccountDetailContract.SingleEvent.DisplayErrorMessage)
        }
    }


    @Test
    fun `when accountId is valid but not associated to any account, it shows an error message`() =
        runTest {
            val accountId = "12"

            val flow = MutableSharedFlow<AccountDetail?>()
            coEvery { usecase.getAccountDetails(accountId) } returns flow
            vm = AccountDetailViewModel(
                accountId = accountId,
                getAccountUsecase = usecase,
                ioDispatcher = dispatcher,
                tracker = tracker
            )

            vm.singleEvent.test {
                flow.emit(null)
                val res = awaitItem()
                Assert.assertTrue(res is AccountDetailContract.SingleEvent.DisplayErrorMessage)
            }
        }

    @Test
    fun `When an error is thrown, it show the error on the UI`() = runTest {
        val accountId = "12"
        coEvery { usecase.getAccountDetails(accountId) }.throws(Exception("error"))
        vm = AccountDetailViewModel(
            accountId = accountId,
            getAccountUsecase = usecase,
            ioDispatcher = dispatcher,
            tracker = tracker
        )

        vm.singleEvent.test {
            val res = awaitItem()
            Assert.assertTrue(res is AccountDetailContract.SingleEvent.DisplayErrorMessage)
        }
    }

}