package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.savedaccount.SavedAccountRemoteDataSource
import com.team6.connectbca.data.datasource.services.SavedAccountService
import com.team6.connectbca.data.model.response.SavedAccountsResponse
import com.team6.connectbca.data.model.response.SavedAccountResponse
import com.team6.connectbca.data.model.body.SaveAccountRequest
import com.team6.connectbca.data.model.body.UpdateFavoriteRequest

class SavedAccountRemoteDataSourceImpl(
    private val savedAccountService: SavedAccountService
) : SavedAccountRemoteDataSource {
    override suspend fun getSavedAccounts(q: String, isFavorite: Boolean): SavedAccountsResponse {
        return savedAccountService.getSavedAccounts(q, isFavorite)
    }

    override suspend fun saveAccount(request: SaveAccountRequest): SavedAccountResponse {
        return savedAccountService.saveAccount(request)
    }

    override suspend fun getSavedAccountDetail(savedBeneficiaryId: String): SavedAccountResponse {
        return savedAccountService.getSavedAccount(savedBeneficiaryId)
    }

    override suspend fun updateFavorite(
        savedBeneficiaryId: String,
        request: UpdateFavoriteRequest
    ): SavedAccountResponse {
        return savedAccountService.updateFavorite(savedBeneficiaryId, request)
    }
}