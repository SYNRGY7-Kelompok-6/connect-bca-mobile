package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.SavedAccount
import com.team6.connectbca.domain.repository.SavedAccountRepository

class GetSavedAccountDetailUseCase(private val savedAccountRepository: SavedAccountRepository) {
    suspend operator fun invoke(savedBeneficiaryId: String): SavedAccount {
        return savedAccountRepository.getSavedAccountDetail(savedBeneficiaryId)
    }
}