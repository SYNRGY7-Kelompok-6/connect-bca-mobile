package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.TRANSFER_DETAIL
import com.team6.connectbca.data.TRASNFER_INTRABANK_URL
import com.team6.connectbca.data.model.body.TransferBody
import com.team6.connectbca.data.model.response.TransactionDetailResponse
import com.team6.connectbca.data.model.response.TransferIntrabankResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface TransferService {
    @GET(TRANSFER_DETAIL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getTransactionDetail(
        @Query("id_transaction") idTransaction: String,
    ): TransactionDetailResponse

    @POST(TRASNFER_INTRABANK_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun transfer(
        @Header("X-PIN-TOKEN") pinToken: String,
        @Body transferBody: TransferBody
    ): TransferIntrabankResponse
}
