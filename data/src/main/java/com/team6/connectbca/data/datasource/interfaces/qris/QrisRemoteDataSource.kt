package com.team6.connectbca.data.datasource.interfaces.qris

import com.team6.connectbca.data.model.body.QrVerifyBody
import com.team6.connectbca.data.model.response.QrVerifyResponse

interface QrisRemoteDataSource {
    suspend fun postVerifyQr(token: String, qrVerifyBody: QrVerifyBody): QrVerifyResponse
}