package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.model.bankstatement.BankStatementResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BalanceInquiryService {

    @GET("balanceInquiry")
    suspend fun getBalanceInquiry(
        @Query("accountNo") accountNo: String
    ): BankStatementResponse
}