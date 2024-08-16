package com.team6.connectbca.domain.model

data class Transfer(
    val status: String? = null,
    val message: String? = null,
    val data: TransferData? = null
)

data class TransferData(
    val refNumber: String? = null,
    val transactionId: String? = null,
    val amount: TransactionAmount? = null,
    val transactionDate: String? = null,
    val remark: String? = null,
    val desc: String? = null,
    val beneficiaryAccountNumber: String? = null,
    val beneficiaryName: String? = null,
    val sourceAccountNumber: String? = null,
    val sourceName: String? = null
)

data class TransactionAmount(
    val value: Int? = null,
    val currency: String? = null
)