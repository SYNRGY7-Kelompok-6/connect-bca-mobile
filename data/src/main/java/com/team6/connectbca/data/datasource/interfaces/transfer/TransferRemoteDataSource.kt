package com.team6.connectbca.data.datasource.interfaces.transfer

import com.team6.connectbca.data.model.response.TransactionDetailResponse

interface TransferRemoteDataSource {
    suspend fun getTransactionDetail(transactionId: String) : TransactionDetailResponse
}