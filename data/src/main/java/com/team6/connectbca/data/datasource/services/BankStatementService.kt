package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.BANK_STATEMENT_URL
import com.team6.connectbca.data.LATEST_INCOME
import com.team6.connectbca.data.model.response.BankStatementResponse
import com.team6.connectbca.data.model.response.TransactionResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface BankStatementService {
    @GET(BANK_STATEMENT_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getBankStatement(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("page") page: String = "0",
        @Query("pageSize") pageSize: String = "1"
    ): BankStatementResponse

    @GET(LATEST_INCOME)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getLatestIncomeTransaction(
    ): TransactionResponse
}
