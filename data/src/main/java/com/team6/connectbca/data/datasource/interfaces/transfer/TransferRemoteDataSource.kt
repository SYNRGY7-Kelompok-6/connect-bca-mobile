package com.team6.connectbca.data.datasource.interfaces.transfer

import com.team6.connectbca.data.model.body.TransferBody
import com.team6.connectbca.data.model.response.TransactionDetailResponse
import com.team6.connectbca.data.model.response.TransferIntrabankResponse

interface TransferRemoteDataSource {
    suspend fun getTransactionDetail(transactionId: String) : TransactionDetailResponse
    suspend fun transfer(pinToken: String, transferBody: TransferBody): TransferIntrabankResponse
}