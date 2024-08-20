package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.SavedAccount
import com.team6.connectbca.domain.repository.SavedAccountRepository

class UpdateFavoriteUseCase(private val savedAccountRepository: SavedAccountRepository) {
    suspend operator fun invoke(savedBeneficiaryId: String, isFavorite: Boolean): UpdateFavoriteInfo {
        return try {
            val updatedAccount = savedAccountRepository.updateFavorite(savedBeneficiaryId, isFavorite)
            UpdateFavoriteInfo.Success(updatedAccount)
        } catch (e: Exception) {
            UpdateFavoriteInfo.Failure(e.message ?: "Unknown error")
        }
    }

    sealed class UpdateFavoriteInfo {
        data class Success(val savedAccount: SavedAccount) : UpdateFavoriteInfo()
        data class Failure(val errorMessage: String) : UpdateFavoriteInfo()
    }
}