package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.SAVED_ACCOUNTS_DETAIL
import com.team6.connectbca.data.SAVED_ACCOUNTS_URL
import com.team6.connectbca.data.model.response.SavedAccountsResponse
import com.team6.connectbca.data.model.response.SavedAccountResponse
import com.team6.connectbca.data.model.body.SaveAccountRequest
import com.team6.connectbca.data.model.body.UpdateFavoriteRequest
import retrofit2.http.*

interface SavedAccountService {
    @GET(SAVED_ACCOUNTS_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getSavedAccounts(
        @Query("q") q: String,
        @Query("isFavorite") isFavorite: Boolean
    ): SavedAccountsResponse

    @POST(SAVED_ACCOUNTS_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun saveAccount(
        @Body request: SaveAccountRequest
    ): SavedAccountResponse

    @GET(SAVED_ACCOUNTS_DETAIL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getSavedAccount(
        @Path("savedBeneficiaryId") savedBeneficiaryId: String
    ): SavedAccountResponse

    @PATCH(SAVED_ACCOUNTS_DETAIL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun updateFavorite(
        @Path("savedBeneficiaryId") savedBeneficiaryId: String,
        @Body request: UpdateFavoriteRequest
    ): SavedAccountResponse
}