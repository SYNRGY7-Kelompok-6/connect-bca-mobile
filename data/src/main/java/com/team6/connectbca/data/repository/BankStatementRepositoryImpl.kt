package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.bankstatement.BankStatementRemoteDataSource
import com.team6.connectbca.domain.model.BankStatementData
import com.team6.connectbca.domain.repository.BankStatementRepository

class BankStatementRepositoryImpl(
    private val remoteDataSource: BankStatementRemoteDataSource
) : BankStatementRepository {
    override suspend fun getBankStatement(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ) : BankStatementData {
        return remoteDataSource.getBankStatement(token, fromDate, toDate, page, pageSize).toEntity().data
    }
}