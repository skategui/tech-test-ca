package com.android.banks.network

import com.android.common.model.AccountDetail
import com.android.common.model.Banks
import com.android.common.model.Operation
import com.google.gson.annotations.SerializedName


// I am only mapping the fields that I really need in the app.
// if some fields are not actually used, I would tell the back-end to only send the payload necessary to the front end (to not leak any useless infos + make the payload smaller, then quicker to deserialize)
data class OperationResponse(
    @SerializedName("amount") val amount: String,
    @SerializedName("date") val timestamp: String,
    @SerializedName("title") val title: String
)

data class AccountDetailResponse(
    @SerializedName("balance") val balance: Float,
    @SerializedName("id") val id: String,
    @SerializedName("label") val label: String,
    @SerializedName("operations") val operations: List<OperationResponse>,
)

data class BanksResponse(
    @SerializedName("accounts") val accounts: List<AccountDetailResponse>,
    @SerializedName("isCA") val isCA: Int,
    @SerializedName("name") val name: String,
)

fun OperationResponse.toOperation(): Operation {

    val timestamp = try {
        this.timestamp.toLong()
    } catch (e: Exception) {
        throw Error("Format received for timestamp not correct")
    }

    val amount = try {
        this.amount.replace(',', '.').toFloat()
    } catch (e: Exception) {
        throw Error("Format received for amount not correct")
    }

    return Operation(
        amount = amount,
        timestamp = timestamp, title = this.title
    )
}

fun AccountDetailResponse.toAccountDetail(): AccountDetail {
    return AccountDetail(balance = this.balance,
        id = this.id,
        title = this.label,
        operations = this.operations.map { it.toOperation() })
}

fun BanksResponse.toBanks(): Banks {

    if (this.isCA < 0 || this.isCA > 1)
        throw Error("Value not correct for field isCa")

    return Banks(
        accounts = this.accounts.map { it.toAccountDetail() },
        isCA = this.isCA == 1,
        name = this.name
    )
}