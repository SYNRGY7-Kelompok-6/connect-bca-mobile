package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.USER_PROFILE
import com.team6.connectbca.data.model.response.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT

interface UserService {
    @GET(USER_PROFILE)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getUserProfile() : UserProfileResponse

    @PUT(USER_PROFILE)
    suspend fun updateUserProfile(
        @Body updateProfileBody: MultipartBody
    ): UserProfileResponse
}