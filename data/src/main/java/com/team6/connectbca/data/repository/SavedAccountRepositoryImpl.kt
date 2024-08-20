package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.savedaccount.SavedAccountRemoteDataSource
import com.team6.connectbca.data.model.body.SaveAccountRequest
import com.team6.connectbca.data.model.body.UpdateFavoriteRequest
import com.team6.connectbca.domain.model.SavedAccounts
import com.team6.connectbca.domain.model.SavedAccount
import com.team6.connectbca.domain.repository.SavedAccountRepository

class SavedAccountRepositoryImpl(
    private val savedAccountRemoteDataSource: SavedAccountRemoteDataSource
) : SavedAccountRepository {
    override suspend fun getSavedAccounts(q: String, isFavorite: Boolean): SavedAccounts {
        return savedAccountRemoteDataSource.getSavedAccounts(q, isFavorite).toEntity()
    }

    override suspend fun saveAccount(beneficiaryAccountNumber: String): SavedAccount {
        val request = SaveAccountRequest(beneficiaryAccountNumber = beneficiaryAccountNumber)
        return savedAccountRemoteDataSource.saveAccount(request).toEntity()
    }

    override suspend fun getSavedAccountDetail(savedBeneficiaryId: String): SavedAccount {
        return savedAccountRemoteDataSource.getSavedAccountDetail(savedBeneficiaryId).toEntity()
    }

    override suspend fun updateFavorite(
        savedBeneficiaryId: String,
        isFavorite: Boolean
    ): SavedAccount {
        val request = UpdateFavoriteRequest(isFavorite = isFavorite)
        return savedAccountRemoteDataSource.updateFavorite(savedBeneficiaryId, request).toEntity()
    }
}