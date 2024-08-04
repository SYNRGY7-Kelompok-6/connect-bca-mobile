package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.DailyTransaction
import com.team6.connectbca.domain.model.MonthTransactionDate
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.domain.repository.BankStatementRepository

class GetThisMonthMutationUseCase(
    private val bankStatementRepository: BankStatementRepository
) {
    suspend operator fun invoke(
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): List<DailyTransaction>? {
        return bankStatementRepository.getTransactionGroups(fromDate, toDate, page, pageSize)
    }
}