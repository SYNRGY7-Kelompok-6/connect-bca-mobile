package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface LoginService {
    @GET("login")
    @Headers(
        "accept: application/json",
        "x-api-key: $API_KEY"
    )
    suspend fun userLogin(
        @Query("username") username: String,
        @Query("password") password: String,
    ) : UserResponse?
}