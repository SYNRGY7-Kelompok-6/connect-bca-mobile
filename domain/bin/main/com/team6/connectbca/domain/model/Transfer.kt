package com.team6.connectbca.domain.model

data class TransactionDetail(
    val data: TransactionDetailData? = null,
    val message: String? = null,
    val status: String? = null
)

data class TransactionDetailData(
    val amount: Int? = null,
    val beneficiaryAccountNumber: String? = null,
    val refNumber: String? = null,
    val beneficiaryName: String? = null,
    val remark: String? = null,
    val sourceName: String? = null,
    val transactionDate: Long? = null,
    val type: String? = null,
    val sourceAccountNumber: String? = null,
    val transactionId: String? = null,
    val desc: String? = null
)