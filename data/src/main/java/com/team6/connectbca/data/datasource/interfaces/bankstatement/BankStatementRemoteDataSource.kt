package com.team6.connectbca.data.datasource.interfaces.bankstatement

import com.team6.connectbca.data.model.response.BankStatementResponse


interface BankStatementRemoteDataSource {
    suspend fun getBankStatement(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): BankStatementResponse
}
