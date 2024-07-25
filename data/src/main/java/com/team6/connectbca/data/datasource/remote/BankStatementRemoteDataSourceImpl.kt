package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.bankstatement.BankStatementRemoteDataSource
import com.team6.connectbca.data.datasource.services.BankStatementService
import com.team6.connectbca.data.model.bankstatement.BankStatementResponse

class BankStatementRemoteDataSourceImpl(
    private val bankStatementService: BankStatementService
) : BankStatementRemoteDataSource {
    override suspend fun getBankStatement(fromDate: String, toDate: String): BankStatementResponse {
        return bankStatementService.getBankStatement(fromDate, toDate).body()!!
    }
}