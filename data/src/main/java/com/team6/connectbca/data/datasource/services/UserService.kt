package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.USER_PROFILE
import com.team6.connectbca.data.model.response.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserService {
    @GET(USER_PROFILE)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun getUserProfile() : UserProfileResponse

    @Multipart
    @PUT(USER_PROFILE)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun updateUserProfile(
        @Part("name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("birth") birth: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part image: MultipartBody.Part?
    ): UserProfileResponse
}