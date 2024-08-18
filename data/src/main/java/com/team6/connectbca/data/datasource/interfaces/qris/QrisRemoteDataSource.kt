package com.team6.connectbca.data.datasource.interfaces.qris

import com.team6.connectbca.data.model.body.QrVerifyBody
import com.team6.connectbca.data.model.body.ShowQrBody
import com.team6.connectbca.data.model.body.TransferAmountBody
import com.team6.connectbca.data.model.body.TransferBody
import com.team6.connectbca.data.model.response.QrVerifyResponse
import com.team6.connectbca.data.model.response.QrisTransferResponse
import com.team6.connectbca.data.model.response.ShowQrResponse

interface QrisRemoteDataSource {
    suspend fun postVerifyQr(token: String, qrVerifyBody: QrVerifyBody): QrVerifyResponse

    suspend fun getShowQr(showQrBody: ShowQrBody): ShowQrResponse

    suspend fun postQrisTransfer(pinToken: String, transferBody: TransferBody): QrisTransferResponse
}