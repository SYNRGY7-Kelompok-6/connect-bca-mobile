package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.qris.QrisRemoteDataSource
import com.team6.connectbca.data.datasource.services.QrisService
import com.team6.connectbca.data.model.body.QrVerifyBody
import com.team6.connectbca.data.model.response.QrVerifyResponse
import com.team6.connectbca.data.model.response.ShowQrResponse

class QrisRemoteDataSourceImpl(private val qrisService: QrisService) : QrisRemoteDataSource {

    override suspend fun postVerifyQr(token: String, qrVerifyBody: QrVerifyBody): QrVerifyResponse {
        return qrisService.verifyQr(qrVerifyBody);
    }

    override suspend fun getShowQr(mode: String, option: String): ShowQrResponse {
        return qrisService.showQr(mode, option);
    }
}