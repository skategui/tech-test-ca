package com.android.banks.datastore

import com.android.common.datastore.BanksDatastore
import com.android.common.model.AccountDetail
import com.android.common.model.Banks
import io.mockk.MockKAnnotations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class BanksDatastoreTest {


    private lateinit var datastore: BanksDatastore

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        datastore = BanksDatastoreImpl()
    }

    @Test
    fun `When storing banks, then the latest version should be emmited`() = runTest {

        val banks = listOf(
            Banks(
                isCA = true, name = "Boursorama", accounts = listOf(
                    AccountDetail(
                        balance = 100.23f,
                        id = "id",
                        title = "label",
                        operations = listOf()
                    )
                )
            )
        )
        val withEmptyValue = datastore.banks().first()
        Assert.assertEquals(emptyList<Banks>(), withEmptyValue)

        datastore.storeBanks(banks)

        val withValue = datastore.banks().first()
        Assert.assertEquals(banks, withValue)
    }

}