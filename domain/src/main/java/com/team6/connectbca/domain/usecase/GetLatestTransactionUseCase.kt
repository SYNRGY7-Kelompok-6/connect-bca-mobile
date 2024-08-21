package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.Transaction
import com.team6.connectbca.domain.repository.BankStatementRepository

class GetLatestTransactionUseCase(private val bankStatementRepository: BankStatementRepository) {
    suspend operator fun invoke(): Transaction? {
        return bankStatementRepository.getLatestIncomeTransaction()
    }
}