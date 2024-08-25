package com.team6.connectbca.data.repository

import android.util.Log
import com.team6.connectbca.data.datasource.interfaces.transfer.TransferRemoteDataSource
import com.team6.connectbca.data.model.body.TransferAmountBody
import com.team6.connectbca.data.model.body.TransferBody
import com.team6.connectbca.data.model.response.TransferIntrabankResponse
import com.team6.connectbca.domain.model.TransactionDetail
import com.team6.connectbca.domain.model.Transfer
import com.team6.connectbca.domain.repository.TransferRepository

class TransferRepositoryImpl(
    private val transferRemoteDataSource: TransferRemoteDataSource
) : TransferRepository {
    override suspend fun getTransactionDetail(transactionId: String): TransactionDetail {
        return transferRemoteDataSource.getTransactionDetail(transactionId).toEntity()
    }

    override suspend fun transfer(
        pinToken: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amountValue: Double,
        currency: String
    ): Transfer {
        val transferBody = TransferBody(
            beneficiaryAccountNumber = beneficiaryAccountNumber,
            remark = remark,
            desc = desc,
            amount = TransferAmountBody(value = amountValue, currency = currency)
        )
        var dataResponse: TransferIntrabankResponse? = null
        try {
            val response: TransferIntrabankResponse =
                transferRemoteDataSource.transfer(pinToken, transferBody)
            dataResponse = response
        } catch (error: Throwable) {
            Log.e("Failed with", error.toString())
            error.printStackTrace()
        }
        return dataResponse?.toEntity() ?: Transfer(
            status = "",
            message = "",
            data = null
        )
    }
}