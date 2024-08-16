package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.transfer.TransferRemoteDataSource
import com.team6.connectbca.domain.model.TransactionDetail
import com.team6.connectbca.domain.repository.TransferRepository

class TransferRepositoryImpl(
    private val transferRemoteDataSource: TransferRemoteDataSource
) : TransferRepository {
    override suspend fun getTransactionDetail(transactionId: String): TransactionDetail {
        return transferRemoteDataSource.getTransactionDetail(transactionId).toEntity()
    }
}