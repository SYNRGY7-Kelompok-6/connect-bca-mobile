package com.team6.connectbca.data.datasource.interfaces.bankstatement

import com.team6.connectbca.data.model.bankstatement.BankStatementResponse

interface BankStatementRemoteDataSource {
    suspend fun getBankStatement(fromDate: String, toDate: String): BankStatementResponse
}
