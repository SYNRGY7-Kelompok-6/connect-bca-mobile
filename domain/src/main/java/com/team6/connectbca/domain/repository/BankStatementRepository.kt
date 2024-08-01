package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.BankStatementData

interface BankStatementRepository {
    suspend fun getBankStatement(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ) : BankStatementData?
}