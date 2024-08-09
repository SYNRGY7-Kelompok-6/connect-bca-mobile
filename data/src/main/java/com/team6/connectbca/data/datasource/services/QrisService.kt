package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.GENERATE_QR_URL
import retrofit2.http.POST
import com.team6.connectbca.data.VERIFY_QR_URL
import com.team6.connectbca.data.model.body.QrVerifyBody
import com.team6.connectbca.data.model.response.QrVerifyResponse
import com.team6.connectbca.data.model.response.ShowQrResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface QrisService {
    @POST(VERIFY_QR_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun verifyQr(
        @Body qrVerifyBody: QrVerifyBody
    ): QrVerifyResponse

    @GET(GENERATE_QR_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun showQr(
        @Query("mode") mode: String,
        @Query("option") option: String
    ): ShowQrResponse
}