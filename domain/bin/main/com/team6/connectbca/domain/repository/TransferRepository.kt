package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.TransactionDetail

interface TransferRepository {
    suspend fun getTransactionDetail(transactionId: String) : TransactionDetail
}