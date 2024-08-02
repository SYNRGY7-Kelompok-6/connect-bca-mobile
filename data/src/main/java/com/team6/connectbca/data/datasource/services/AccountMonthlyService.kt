package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.ACCOUNT_MONTHLY_URL
import com.team6.connectbca.data.model.response.AccountMonthlyResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface AccountMonthlyService {
    @GET(ACCOUNT_MONTHLY_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getAccountMonthly(
        @Header("Authorization") token: String,
        @Query("month") month: String = "1"
    ): AccountMonthlyResponse
}