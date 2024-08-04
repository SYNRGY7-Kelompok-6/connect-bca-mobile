package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.BankStatementData
import com.team6.connectbca.domain.model.DailyTransaction

interface BankStatementRepository {
    suspend fun getBankStatement(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ) : BankStatementData?

    suspend fun getTransactionGroups(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ) : List<DailyTransaction>?
}