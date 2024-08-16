package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.TransactionAmount
import com.team6.connectbca.domain.model.Transfer

interface TransferRepository {
    suspend fun makeTransfer(
        token: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amount: TransactionAmount
    ): Transfer
}