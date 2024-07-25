package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.BANK_STATEMENT_URL
import com.team6.connectbca.data.model.bankstatement.BankStatementResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BankStatementService {
    @GET(BANK_STATEMENT_URL)
    suspend fun getBankStatement(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Response<BankStatementResponse>
}
