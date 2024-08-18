package com.team6.connectbca.data.repository

import android.util.Log
import com.team6.connectbca.data.datasource.interfaces.qris.QrisRemoteDataSource
import com.team6.connectbca.data.model.body.QrVerifyBody
import com.team6.connectbca.data.model.body.ShowQrBody
import com.team6.connectbca.data.model.body.TransferBody
import com.team6.connectbca.data.model.body.TransferAmountBody
import com.team6.connectbca.data.model.response.QrVerifyResponse
import com.team6.connectbca.data.model.response.QrisTransferResponse
import com.team6.connectbca.data.model.response.ShowQrResponse
import com.team6.connectbca.domain.model.QrVerify
import com.team6.connectbca.domain.model.QrVerifyData
import com.team6.connectbca.domain.model.QrisTransfer
import com.team6.connectbca.domain.model.ShowQr
import com.team6.connectbca.domain.model.TransferAmount
import com.team6.connectbca.domain.repository.QrisRepository

class QrisRepositoryImpl(private val qrisRemoteDataSource: QrisRemoteDataSource) : QrisRepository {

    override suspend fun verifyQr(token: String, qris: String): QrVerify {
        val verifyBody = QrVerifyBody(payload = qris)
        var data: QrVerifyResponse? = null
        try {
            val response: QrVerifyResponse = qrisRemoteDataSource.postVerifyQr(token, verifyBody)
            data = response
        } catch (error: Throwable) {
            Log.e("Failed with", error.toString())
        }
        return data?.toEntity() ?: QrVerify(
            status = "",
            message = "",
            data = QrVerifyData(beneficiaryAccountNumber = "", beneficiaryName = "", remark = "")
        )
    }

    override suspend fun showQr(amountValue: Double, currency: String): ShowQr {
        val amount = ShowQrBody(amount = TransferAmountBody(value = amountValue, currency = currency))
        var data: ShowQrResponse? = null
        try {
            val response: ShowQrResponse = qrisRemoteDataSource.getShowQr(amount)
            data = response
        } catch (error: Throwable) {
            Log.e("Failed with", error.toString())
        }
        return data?.toEntity() ?: ShowQr(
            status = "",
            message = "",
            data = null
        )
    }

    override suspend fun qrisTransfer(
        pinToken: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amountValue: Double,
        currency: String
    ): QrisTransfer {
        var transferBody = TransferBody(
            beneficiaryAccountNumber = beneficiaryAccountNumber,
            remark = remark,
            desc = desc,
            amount = TransferAmountBody(value = amountValue, currency = currency)
        )
        var dataResponse: QrisTransferResponse? = null
        try {
            val response: QrisTransferResponse =
                qrisRemoteDataSource.postQrisTransfer(pinToken, transferBody)
            dataResponse = response
        } catch (error: Throwable) {
            Log.e("Failed with", error.toString())
        }
        return dataResponse?.toEntity() ?: QrisTransfer(
            status = "",
            message = "",
            data = null
        )
    }

}