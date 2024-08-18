package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.transfer.TransferRemoteDataSource
import com.team6.connectbca.data.datasource.services.TransferService
import com.team6.connectbca.data.model.body.TransferBody
import com.team6.connectbca.data.model.response.TransactionDetailResponse
import com.team6.connectbca.data.model.response.TransferIntrabankResponse

class TransferRemoteDataSourceImpl(
    private val transferService: TransferService
) : TransferRemoteDataSource {
    override suspend fun getTransactionDetail(transactionId: String): TransactionDetailResponse {
        return transferService.getTransactionDetail(transactionId)
    }

    override suspend fun transfer(pinToken: String, transferBody: TransferBody): TransferIntrabankResponse {
        return transferService.transfer(pinToken, transferBody)
    }
}