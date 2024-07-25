package com.team6.connectbca.data.model.bankstatement

import com.google.gson.annotations.SerializedName

data class BankStatementResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: BankStatementData
)

data class BankStatementData(
    @SerializedName("accountInfo") val accountInfo: AccountInfo,
    @SerializedName("accountBalance") val accountBalance: AccountBalance,
    @SerializedName("mutations") val mutations: List<Mutation>
)

data class AccountInfo(
    @SerializedName("accountNo") val accountNo: String,
    @SerializedName("accountType") val accountType: String,
    @SerializedName("accountCardExp") val accountCardExp: String,
    @SerializedName("name") val name: String,
    @SerializedName("accountBalance") val accountBalanceDetails: AccountBalanceDetails,
    @SerializedName("accountMonthly") val accountMonthly: AccountMonthly,
    @SerializedName("pinExpiredTimeLeft") val pinExpiredTimeLeft: Int
)

data class AccountBalanceDetails(
    @SerializedName("availableBalance") val availableBalance: CurrencyValue,
    @SerializedName("holdAmount") val holdAmount: CurrencyValue
)

data class AccountMonthly(
    @SerializedName("monthlyIncome") val monthlyIncome: CurrencyValue,
    @SerializedName("monthlyOutcome") val monthlyOutcome: CurrencyValue
)

data class AccountBalance(
    @SerializedName("startingBalance") val startingBalance: CurrencyDateTime,
    @SerializedName("endingBalance") val endingBalance: CurrencyDateTime
)

data class CurrencyValue(
    @SerializedName("value") val value: Double,
    @SerializedName("currency") val currency: String
)

data class CurrencyDateTime(
    @SerializedName("value") val value: Double,
    @SerializedName("currency") val currency: String,
    @SerializedName("dateTime") val dateTime: String
)

data class Mutation(
    @SerializedName("amount") val amount: CurrencyValue,
    @SerializedName("transactionDate") val transactionDate: String,
    @SerializedName("remark") val remark: String,
    @SerializedName("type") val type: String,
    @SerializedName("beneficiaryAccount") val beneficiaryAccount: AccountDetails,
    @SerializedName("sourceAccount") val sourceAccount: AccountDetails
)

data class AccountDetails(
    @SerializedName("beneficiaryAccountNumber") val beneficiaryAccountNumber: String,
    @SerializedName("beneficiaryAccountName") val beneficiaryAccountName: String
)