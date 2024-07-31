package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.domain.repository.BankStatementRepository

class GetMutationUseCase(
    private val bankStatementRepository: BankStatementRepository
) {
    suspend operator fun invoke(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): List<MutationsItem?> {
        return bankStatementRepository.getBankStatement(token, fromDate, toDate, page, pageSize).mutations!!
    }
}