package com.team6.connectbca.domain.model

data class Transaction(
    val message: String? = null,
    val status: String? = null,
    val data: List<TransactionData>? = null
)

data class TransactionData(
    val sourceName : String? = null,
    val amount: Double? = null,
    val transactionDate : String? = null,
)
