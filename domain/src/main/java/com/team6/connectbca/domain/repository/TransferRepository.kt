package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.TransactionDetail
import com.team6.connectbca.domain.model.Transfer

interface TransferRepository {
    suspend fun getTransactionDetail(transactionId: String) : TransactionDetail

    suspend fun transfer(
        pinToken: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amountValue: Double,
        currency: String
    ): Transfer
}