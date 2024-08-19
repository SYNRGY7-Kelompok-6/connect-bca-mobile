package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.SavedAccount
import com.team6.connectbca.domain.model.SavedAccounts
import com.team6.connectbca.domain.repository.SavedAccountRepository

class GetSavedAccountsUseCase(private val savedAccountRepository: SavedAccountRepository) {
    suspend operator fun invoke(q: String, isFavorite: Boolean): SavedAccounts {
        return savedAccountRepository.getSavedAccounts(q, isFavorite)
    }
}