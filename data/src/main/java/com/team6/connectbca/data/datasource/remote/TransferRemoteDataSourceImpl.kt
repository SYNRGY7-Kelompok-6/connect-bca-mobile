package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.transfer.TransferRemoteDataSource
import com.team6.connectbca.data.datasource.services.TransferService
import com.team6.connectbca.data.model.response.TransactionDetailResponse

class TransferRemoteDataSourceImpl(
    private val transferService: TransferService
) : TransferRemoteDataSource {
    override suspend fun getTransactionDetail(transactionId: String): TransactionDetailResponse {
        return transferService.getTransactionDetail(transactionId)
    }
}