package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.transfer.TransferRemoteDataSource
import com.team6.connectbca.data.datasource.services.TransferService
import com.team6.connectbca.data.model.body.TransferIntrabankRequest
import com.team6.connectbca.data.model.response.TransferIntrabankResponse

class TransferRemoteDataSourceImpl(
    private val transferService: TransferService
): TransferRemoteDataSource {
    override suspend fun makeTransfer(
        token: String,
        request: TransferIntrabankRequest
    ): TransferIntrabankResponse {
        return transferService.makeTransfer(token, request)
    }
}