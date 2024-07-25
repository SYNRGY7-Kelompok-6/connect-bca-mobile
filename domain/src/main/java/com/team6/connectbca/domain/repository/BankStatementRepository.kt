package com.team6.connectbca.domain.repository

import com.team6.connectbca.data.model.bankstatement.BankStatementResponse

interface BankStatementRepository {
    suspend fun getBankStatement(fromDate: String, toDate: String): BankStatementResponse
}