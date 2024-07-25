package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.bankstatement.BankStatementRemoteDataSource
import com.team6.connectbca.domain.repository.BankStatementRepository
import com.team6.connectbca.data.model.bankstatement.BankStatementResponse

class BankStatementRepositoryImpl(
    private val remoteDataSource: BankStatementRemoteDataSource
) : BankStatementRepository {
    override suspend fun getBankStatement(fromDate: String, toDate: String): BankStatementResponse {
        return remoteDataSource.getBankStatement(fromDate, toDate)
    }
}