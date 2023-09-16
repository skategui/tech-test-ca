package com.android.banks.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.android.banks.R
import com.android.common.model.AccountDetail
import com.android.common.model.Banks
import com.android.common.model.Operation
import com.android.common.ui.theme.CreditAgricoleTheme
import com.android.common.utils.twoDigits
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.Date

internal class BanksScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val caBanks = Banks(
        isCA = true, name = "Boursorama", accounts = listOf(
            AccountDetail(
                balance = 100.23f,
                id = "id",
                title = "label",
                operations = listOf(
                    Operation(
                        amount = 100f, timestamp = Date().time, title = "operationTitle"
                    ), Operation(
                        amount = 200f, timestamp = Date().time, title = "operationTitle2"
                    )
                ),
            )
        )
    )

    private val otherBanks = Banks(
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
                operations = listOf(
                    Operation(
                        amount = 101f, timestamp = Date().time, title = "operationTitle3"
                    ), Operation(
                        amount = 202f, timestamp = Date().time, title = "operationTitle4"
                    )
                ),
            )
        )
    )


    @Test
    fun WhenLoadingDataIfRendersTheBankInfos() {

        var onAccountClicked = 0

        // Start the app
        composeTestRule.setContent {
            CreditAgricoleTheme {
                BanksScreenUI(
                    modifier = Modifier,
                    caBanks = listOf(caBanks),
                    otherBanks = listOf(otherBanks),
                    onAccountClicked = { onAccountClicked++ }
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.my_accounts))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.credit_agricole_label))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.other_banks_label))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(caBanks.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(otherBanks.name).assertIsDisplayed()
        composeTestRule.onNodeWithText("${caBanks.accounts.map { it.balance }.sum().twoDigits()} €")
            .assertIsDisplayed()


        composeTestRule.onNodeWithText(
            "${
                otherBanks.accounts.map { it.balance }.sum().twoDigits()
            } €"
        )
            .assertIsDisplayed()



        caBanks.accounts.map { account ->
            composeTestRule.onNodeWithText(
                account.title
            ).assertDoesNotExist()
        }
        otherBanks.accounts.map { account ->
            composeTestRule.onNodeWithText(
                account.title
            ).assertDoesNotExist()
        }

        composeTestRule.onNodeWithText(caBanks.name).performClick()

        caBanks.accounts.map { account ->
            composeTestRule.onNodeWithText(
                account.title
            ).assertIsDisplayed()

            composeTestRule.onAllNodesWithText(
                "${account.balance.twoDigits()} €"
            )[1].assertIsDisplayed()
        }


        composeTestRule.onNodeWithText(otherBanks.name).performClick()

        otherBanks.accounts.map { account ->
            composeTestRule.onNodeWithText(
                account.title
            ).assertIsDisplayed()

            composeTestRule.onNodeWithText(
                "${account.balance.twoDigits()} €"
            ).assertIsDisplayed()
        }

        composeTestRule.onNodeWithText(otherBanks.accounts[0].title).performClick()
        Assert.assertEquals(onAccountClicked, 1)
    }
}