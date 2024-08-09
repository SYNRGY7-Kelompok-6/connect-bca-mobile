package com.team6.connectbca.data.datasource.interfaces.qris

import com.team6.connectbca.data.model.body.QrVerifyBody
import com.team6.connectbca.data.model.response.QrVerifyResponse
import com.team6.connectbca.data.model.response.ShowQrResponse

interface QrisRemoteDataSource {
    suspend fun postVerifyQr(token: String, qrVerifyBody: QrVerifyBody): QrVerifyResponse

    suspend fun getShowQr(mode:String, option:String): ShowQrResponse
}