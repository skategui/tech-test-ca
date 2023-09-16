package com.android.banks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.common.model.AccountDetail
import com.android.common.ui.CADivider
import com.android.common.ui.PricingRowItem
import com.android.common.ui.TitleRowItem
import com.android.common.ui.theme.MidBlack
import com.android.common.utils.twoDigits

@Composable
internal fun AccountsDetailList(accounts: List<AccountDetail>, onAccountClicked: (String) -> Unit) {
    val accountsSorted = accounts.sortedBy { it.title.lowercase() }
    Column {
        accountsSorted.forEach { account ->
            AccountDetailItem(account, onAccountClicked)
        }
    }
}

@Composable
private fun AccountDetailItem(account: AccountDetail, onAccountClicked: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = Color.White)
            .clickable { onAccountClicked.invoke(account.id) }
    ) {

        TitleRowItem(
            title = account.title,
            modifier = Modifier
                .padding(start = 50.dp)
                .align(Alignment.CenterStart)
        )


        PricingRowItem(
            balance = account.balance,
            modifier = Modifier
                .padding(end = 50.dp)
                .align(Alignment.CenterEnd)
        )

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "See account detail",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(end = 20.dp)
                .align(Alignment.CenterEnd)
        )

        CADivider(
            paddingStart = 50.dp,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}