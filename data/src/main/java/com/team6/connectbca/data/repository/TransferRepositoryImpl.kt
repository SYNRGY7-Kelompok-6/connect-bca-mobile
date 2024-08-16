package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.transfer.TransferRemoteDataSource
import com.team6.connectbca.data.model.body.TransferIntrabankRequest
import com.team6.connectbca.data.model.response.Amount
import com.team6.connectbca.domain.model.TransactionAmount
import com.team6.connectbca.domain.model.Transfer
import com.team6.connectbca.domain.repository.TransferRepository

class TransferRepositoryImpl(
    private val transferRemoteDataSource: TransferRemoteDataSource
): TransferRepository {
    override suspend fun makeTransfer(
        token: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amount: TransactionAmount
    ): Transfer {
        val request = TransferIntrabankRequest(
            beneficiaryAccountNumber = beneficiaryAccountNumber,
            remark = remark,
            desc = desc,
            amount = Amount(
                value = amount.value ?: 0,
                currency = amount.currency ?: "IDR"
            )
        )
        val response = transferRemoteDataSource.makeTransfer(token, request)
        return response.toEntity()
    }
}