package com.android.common.model

data class Operation(
    val amount: Float,
    val timestamp: Long,
    val title: String
)

data class AccountDetail(
    val balance: Float,
    val id: String,
    val title: String,
    val operations: List<Operation>,
)

data class Banks(
    val accounts: List<AccountDetail>,
    val isCA: Boolean,
    val name: String,
)