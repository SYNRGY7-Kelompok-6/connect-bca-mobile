package com.team6.connectbca.data.repository

import android.util.Log
import com.team6.connectbca.data.datasource.interfaces.qris.QrisRemoteDataSource
import com.team6.connectbca.data.model.body.QrVerifyBody
import com.team6.connectbca.data.model.response.QrVerifyResponse
import com.team6.connectbca.data.model.response.QrVerifyDataResponse
import com.team6.connectbca.domain.model.QrVerify
import com.team6.connectbca.domain.model.QrVerifyData
import com.team6.connectbca.domain.repository.QrisRepository

class QrisRepositoryImpl(private val qrisRemoteDataSource: QrisRemoteDataSource) : QrisRepository {

    override suspend fun verifyQr(token:String, qris: String): QrVerify {
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

}