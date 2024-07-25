package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.repository.BankStatementRepository
import com.team6.connectbca.data.model.bankstatement.BankStatementResponse

class GetBankStatementUseCase(private val bankStatementRepository: BankStatementRepository) {
    suspend fun execute(fromDate: String, toDate: String): BankStatementResponse {
        return bankStatementRepository.getBankStatement(fromDate, toDate)
    }
}
