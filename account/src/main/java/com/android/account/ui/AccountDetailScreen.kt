package com.android.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.android.account.vm.AccountDetailContract
import com.android.account.vm.AccountDetailViewModel
import com.android.common.R
import com.android.common.model.Operation
import com.android.common.ui.CADivider
import com.android.common.ui.ErrorPopup
import com.android.common.ui.LoadingState
import com.android.common.ui.PricingRowItem
import com.android.common.ui.TitleRowItem
import com.android.common.ui.theme.BackgroundColor
import com.android.common.ui.theme.LightBlack
import com.android.common.utils.twoDigits
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview
@Composable
fun AccountDetailScreenPreview() {

    AccountDetailScreenUI(modifier = Modifier,
        balance = 100.23f,
        title = "Compte depot",
        operations = listOf(
            Operation(
                amount = "100", timestamp = Date().time, title = "operationTitle"
            ), Operation(
                amount = "200", timestamp = Date().time, title = "operationTitle2"
            )
        ),
        onBackPressed = {})
}

@Composable
fun AccountDetailScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: AccountDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val displayErrorMessageState = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.singleEvent.collectLatest { singleEvent ->
            when (singleEvent) {
                AccountDetailContract.SingleEvent.DisplayErrorMessage -> displayErrorMessageState.value =
                    true
            }
        }
    }

    if (displayErrorMessageState.value) {
        ErrorPopup(message = stringResource(R.string.generic_error_msg), closeSelection = {
            displayErrorMessageState.value = false
        })
    }

    // LOADER
    if (uiState.isLoading) {
        LoadingState(
            modifier = modifier, message = stringResource(id = R.string.loading_in_progress)
        )
    }

    AccountDetailScreenUI(modifier = modifier,
        balance = uiState.balance,
        title = uiState.title,
        operations = uiState.operations,
        onBackPressed = {
            navController.navigateUp()
        })
}

@Composable
fun AccountDetailScreenUI(
    modifier: Modifier,
    balance: Float,
    title: String,
    operations: List<Operation>,
    onBackPressed: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        val (crossRef, balanceRef, titleRef, list) = createRefs()


        Icon(imageVector = Icons.Filled.Close,
            contentDescription = "Close screen",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .constrainAs(crossRef) {
                    top.linkTo(parent.top, margin = 80.dp)
                    start.linkTo(parent.start, margin = 20.dp)
                }
                .clickable { onBackPressed.invoke() })

        Text(
            text = "${balance.twoDigits()} €",
            color = Color.Black,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.constrainAs(balanceRef) {
                top.linkTo(parent.top, margin = 100.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )


        Text(
            text = title,
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(balanceRef.bottom, margin = 40.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )

        OperationsList(operations = operations, modifier = Modifier.constrainAs(list) {
            top.linkTo(titleRef.bottom, margin = 50.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}

@Composable
private fun OperationsList(modifier: Modifier, operations: List<Operation>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        items(operations) { operation ->
            OperationItem(operation)
        }
    }
}

@Composable
private fun OperationItem(operation: Operation) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = Color.White)
    ) {

        TitleRowItem(
            title = operation.title,
            modifier = Modifier.padding(start = 30.dp, top = 15.dp)
        )

        val cleanDate =
            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Date(operation.timestamp * 1000))
        Text(
            text = cleanDate,
            color = LightBlack,
            fontSize = 12.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(start = 30.dp, bottom = 10.dp)
                .align(Alignment.BottomStart)
        )

        PricingRowItem(
            balance = operation.amount,
            modifier = Modifier
                .padding(end = 30.dp)
                .align(Alignment.CenterEnd)
        )

        CADivider(
            paddingStart = 20.dp,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}