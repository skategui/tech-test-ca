package com.android.account.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.android.common.model.AccountDetail
import com.android.common.model.Operation
import com.android.common.ui.theme.CreditAgricoleTheme
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.Date

internal class AccountDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val account = AccountDetail(
        balance = 100.23f,
        id = "id",
        title = "Compte dépot",
        operations = listOf(
            Operation(
                amount = "100", timestamp = Date().time, title = "operationTitle"
            ), Operation(
                amount = "200", timestamp = Date().time, title = "operationTitle2"
            )
        ),
    )


    @Test
    fun WhenLoadingDataIfRendersTheBankInfos() {

        var onBackPressed = 0

        // Start the app
        composeTestRule.setContent {
            CreditAgricoleTheme {
                AccountDetailScreenUI(
                    modifier = Modifier,
                    balance = account.balance,
                    title = account.title,
                    operations = account.operations,
                    onBackPressed = { onBackPressed++ }
                )
            }
        }

        composeTestRule.onNodeWithText(account.title)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("${account.balance} €")
            .assertIsDisplayed()

        account.operations.map { operation ->
            composeTestRule.onNodeWithText(
                operation.title
            ).assertIsDisplayed()

            composeTestRule.onNodeWithText(
                "${operation.amount} €"
            ).assertIsDisplayed()
        }

        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(com.android.account.R.string.content_description_close_screen))
            .performClick()

        Assert.assertEquals(onBackPressed, 1)
    }
}