package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.DailyTransaction
import com.team6.connectbca.domain.repository.BankStatementRepository

class GetDateRangeMutationUseCase(
    private val bankStatementRepository: BankStatementRepository
) {
    suspend operator fun invoke(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): List<DailyTransaction>? {
        return bankStatementRepository.getTransactionGroups(token, fromDate, toDate, page, pageSize)
    }
}