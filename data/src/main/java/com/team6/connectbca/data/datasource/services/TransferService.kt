package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.TRANSFER_INTRABANK_URL
import com.team6.connectbca.data.model.body.TransferIntrabankRequest
import com.team6.connectbca.data.model.response.TransferIntrabankResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TransferService {
    @POST(TRANSFER_INTRABANK_URL)
    suspend fun makeTransfer(
        @Header("X-PIN-TOKEN") token: String,
        @Body request: TransferIntrabankRequest
    ): TransferIntrabankResponse
}