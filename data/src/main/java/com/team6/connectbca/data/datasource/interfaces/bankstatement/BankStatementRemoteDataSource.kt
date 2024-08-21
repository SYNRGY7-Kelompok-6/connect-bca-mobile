package com.team6.connectbca.data.datasource.interfaces.bankstatement

import com.team6.connectbca.data.model.response.BankStatementResponse
import com.team6.connectbca.data.model.response.TransactionResponse


interface BankStatementRemoteDataSource {
    suspend fun getBankStatement(
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): BankStatementResponse

    suspend fun getLatestIncomeTransaction(): TransactionResponse?
}
