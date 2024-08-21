package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.bankstatement.BankStatementRemoteDataSource
import com.team6.connectbca.data.datasource.services.BankStatementService
import com.team6.connectbca.data.model.response.BankStatementResponse
import com.team6.connectbca.data.model.response.TransactionResponse

class BankStatementRemoteDataSourceImpl(
    private val bankStatementService: BankStatementService
) : BankStatementRemoteDataSource {
    override suspend fun getBankStatement(
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): BankStatementResponse {
        return bankStatementService.getBankStatement(
            fromDate,
            toDate,
            page,
            pageSize
        )
    }

    override suspend fun getLatestIncomeTransaction(): TransactionResponse? {
        return bankStatementService.getLatestIncomeTransaction()
    }
}