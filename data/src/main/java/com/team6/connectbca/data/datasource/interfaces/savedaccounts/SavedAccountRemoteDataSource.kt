package com.team6.connectbca.data.datasource.interfaces.savedaccount

import com.team6.connectbca.data.model.response.SavedAccountsResponse
import com.team6.connectbca.data.model.response.SavedAccountResponse
import com.team6.connectbca.data.model.body.SaveAccountRequest
import com.team6.connectbca.data.model.body.UpdateFavoriteRequest

interface SavedAccountRemoteDataSource {
    suspend fun getSavedAccounts(q: String, isFavorite: Boolean): SavedAccountsResponse
    suspend fun saveAccount(request: SaveAccountRequest): SavedAccountResponse

    suspend fun getSavedAccountDetail(savedBeneficiaryId: String): SavedAccountResponse
    suspend fun updateFavorite(savedBeneficiaryId: String, request: UpdateFavoriteRequest): SavedAccountResponse
}