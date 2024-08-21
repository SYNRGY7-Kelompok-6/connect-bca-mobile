package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.BankStatementData
import com.team6.connectbca.domain.model.DailyTransaction
import com.team6.connectbca.domain.model.Transaction

interface BankStatementRepository {
    suspend fun getBankStatement(
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): BankStatementData?

    suspend fun getTransactionGroups(
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): List<DailyTransaction>?

    suspend fun getLatestIncomeTransaction() : Transaction?
}