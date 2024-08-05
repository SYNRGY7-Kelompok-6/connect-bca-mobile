package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.domain.model.BankStatementData
import com.team6.connectbca.domain.repository.BankStatementRepository

class GetBalanceInquiryUseCase(
    private val bankStatementRepository: BankStatementRepository
) {
    suspend operator fun invoke(
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): AccountInfo? {
        return bankStatementRepository.getBankStatement(fromDate, toDate, page, pageSize)?.accountInfo
    }
}