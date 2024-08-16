package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.TRANSFER_DETAIL
import com.team6.connectbca.data.model.response.TransactionDetailResponse
import retrofit2.http.GET
import retrofit2.http.Headers
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
}