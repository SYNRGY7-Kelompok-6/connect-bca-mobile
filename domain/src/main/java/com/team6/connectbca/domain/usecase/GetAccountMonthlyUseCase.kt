package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.domain.model.AccountMonthlyData
import com.team6.connectbca.domain.repository.AccountMonthlyRepository

class GetAccountMonthlyUseCase(
    private val accountMonthlyRepository: AccountMonthlyRepository
) {
    suspend operator fun invoke(
        month: String
    ): AccountMonthlyData? {
        return accountMonthlyRepository.getAccountMonthly(month).data
    }
}