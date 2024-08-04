package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.qris.QrisRemoteDataSource
import com.team6.connectbca.data.datasource.services.QrisService
import com.team6.connectbca.data.model.body.QrVerifyBody
import com.team6.connectbca.data.model.response.QrVerifyResponse

class QrisRemoteDataSourceImpl(private val qrisService: QrisService) : QrisRemoteDataSource {

    override suspend fun postVerifyQr(token: String, qrVerifyBody: QrVerifyBody): QrVerifyResponse {
        return qrisService.verifyQr( qrVerifyBody);
    }
}