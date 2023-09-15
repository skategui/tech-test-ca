package com.android.banks.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.android.banks.R
import com.android.banks.vm.BanksContract
import com.android.banks.vm.BanksViewModel
import com.android.common.model.AccountDetail
import com.android.common.model.Banks
import com.android.common.ui.CADivider
import com.android.common.ui.ErrorPopup
import com.android.common.ui.LoadingState
import com.android.common.ui.PricingRowItem
import com.android.common.ui.TitleRowItem
import com.android.common.ui.screens.Screens
import com.android.common.ui.theme.BackgroundColor
import com.android.common.ui.theme.LightBlack
import com.android.common.ui.theme.LightGrey
import com.android.common.ui.theme.MidGrey
import com.android.common.utils.twoDigits
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun BanksScreenPreview() {

    val caBanks = Banks(
        isCA = true, name = "Boursorama", accounts = listOf(
            AccountDetail(
                balance = 100.23f,
                id = "id",
                title = "label",
                operations = listOf(),
            )
        )
    )

    val otherBanks = Banks(
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

    BanksScreenUI(modifier = Modifier,
        caBanks = listOf(caBanks),
        otherBanks = listOf(otherBanks),
        onAccountClicked = { })
}

@Composable
fun BanksScreen(
    modifier: Modifier, navController: NavController, viewModel: BanksViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val displayErrorMessageState = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.singleEvent.collectLatest { singleEvent ->
            when (singleEvent) {
                is BanksContract.SingleEvent.GoAccountDetailScreen -> navController.navigate(
                    "${Screens.AccountDetailScreen.route}/${singleEvent.accountId}"
                )

                BanksContract.SingleEvent.DisplayErrorMessage -> displayErrorMessageState.value =
                    true
            }
        }
    }

    if (displayErrorMessageState.value) {
        ErrorPopup(
            message = stringResource(com.android.common.R.string.generic_error_msg),
            closeSelection = {
                displayErrorMessageState.value = false
            })
    }

    // LOADER
    if (uiState.isLoading) {
        LoadingState(
            modifier = modifier,
            message = stringResource(id = com.android.common.R.string.loading_in_progress)
        )
    }

    BanksScreenUI(modifier = modifier,
        caBanks = uiState.caBanks,
        otherBanks = uiState.otherBanks,
        onAccountClicked = { accountId ->
            viewModel.setEvent(BanksContract.Interaction.AccountClicked(accountId))
        })
}

@Composable
fun BanksScreenUI(
    modifier: Modifier,
    caBanks: List<Banks>,
    otherBanks: List<Banks>,
    onAccountClicked: (String) -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF7F6F6))
    ) {
        val (title, list) = createRefs()


        Text(
            text = stringResource(R.string.my_accounts),
            color = Color.Black,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 100.dp)
                start.linkTo(parent.start, margin = 20.dp)
            },
        )

        BanksList(grouped = mapOf(
            stringResource(R.string.credit_agricole_label) to caBanks,
            stringResource(R.string.other_banks_label) to otherBanks
        ), onAccountClicked = onAccountClicked, modifier = Modifier.constrainAs(list) {
            top.linkTo(title.bottom, margin = 40.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BanksList(
    modifier: Modifier, grouped: Map<String, List<Banks>>, onAccountClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        grouped.forEach { (title, banksList) ->
            stickyHeader {
                BankHeader(modifier, title)
            }

            items(banksList) { banks ->

                val expanded = remember { mutableStateOf(false) }
                val total = banks.accounts.map { it.balance }.sum()
                BankItem(banks.name, total, expanded)
                if (expanded.value) {
                    AccountsDetailList(banks.accounts, onAccountClicked)
                }
            }
        }
    }
}

@Composable
private fun BankHeader(modifier: Modifier, title: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundColor)
    ) {
        Text(
            text = title,
            color = LightGrey,
            fontSize = 16.sp,
            modifier = modifier
                .height(40.dp)
                .padding(start = 20.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
        )
    }

}

@Composable
private fun BankItem(banksName: String, amountTotal: Float, expanded: MutableState<Boolean>) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = Color.White)
            .clickable { expanded.value = !expanded.value }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
                )
            ),
    ) {

        TitleRowItem(
            title = banksName,
            modifier = Modifier
                .padding(start = 30.dp)
                .align(Alignment.CenterStart)
        )


        PricingRowItem(
            balance = amountTotal.twoDigits(),
            modifier = Modifier
                .padding(end = 50.dp)
                .align(Alignment.CenterEnd)
        )

        Icon(
            imageVector = if (expanded.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = "account detail toggle icon",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterEnd)
        )

        CADivider(
            paddingStart = 20.dp,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}