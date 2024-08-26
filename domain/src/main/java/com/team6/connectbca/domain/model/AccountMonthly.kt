package com.team6.connectbca.domain.model

data class AccountMonthly(
    val data: AccountMonthlyData? = null,
    val message: String? = null,
    val status: String? = null
)

data class MonthlyIncome(
    val currency: String? = null,
    val value: Int? = null
)

data class AccountMonthlyData(
    val monthlyOutcome: MonthlyOutcome? = null,
    val monthlyIncome: MonthlyIncome? = null
)

data class MonthlyOutcome(
    val currency: String? = null,
    val value: Int? = null
)