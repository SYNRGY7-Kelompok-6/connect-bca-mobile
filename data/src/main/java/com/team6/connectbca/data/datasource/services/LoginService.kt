package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.BuildConfig
import com.team6.connectbca.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST(BuildConfig.LOGIN_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String,
    ) : LoginResponse
}