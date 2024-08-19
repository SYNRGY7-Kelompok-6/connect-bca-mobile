package com.team6.connectbca.data.datasource.interfaces.transfer

import com.team6.connectbca.data.model.body.TransferIntrabankRequest
import com.team6.connectbca.data.model.response.TransactionDetailResponse
import com.team6.connectbca.data.model.response.TransferIntrabankResponse

interface TransferRemoteDataSource {
    suspend fun getTransactionDetail(transactionId: String) : TransactionDetailResponse
    suspend fun makeTransfer(token: String, request: TransferIntrabankRequest): TransferIntrabankResponse
}