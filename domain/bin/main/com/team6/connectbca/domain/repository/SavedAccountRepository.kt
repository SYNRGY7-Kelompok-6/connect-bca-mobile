package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.SavedAccounts
import com.team6.connectbca.domain.model.SavedAccount

interface SavedAccountRepository {
    suspend fun getSavedAccounts(q: String, isFavorite: Boolean): SavedAccounts
    suspend fun saveAccount(beneficiaryAccountNumber: String): SavedAccount

    suspend fun getSavedAccountDetail(savedBeneficiaryId: String): SavedAccount
    suspend fun updateFavorite(savedBeneficiaryId: String, isFavorite: Boolean): SavedAccount
}