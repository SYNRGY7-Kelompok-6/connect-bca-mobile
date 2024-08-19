package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.SavedAccount
import com.team6.connectbca.domain.repository.SavedAccountRepository

class SaveAccountUseCase(private val savedAccountRepository: SavedAccountRepository) {
    suspend operator fun invoke(beneficiaryAccountNumber: String): SaveAccountInfo {
        return try {
            val savedAccount = savedAccountRepository.saveAccount(beneficiaryAccountNumber)
            SaveAccountInfo.Success(savedAccount)
        } catch (e: Exception) {
            SaveAccountInfo.Failure(e.message ?: "Unknown error")
        }
    }

    sealed class SaveAccountInfo {
        data class Success(val savedAccount: SavedAccount) : SaveAccountInfo()
        data class Failure(val errorMessage: String) : SaveAccountInfo()
    }
}