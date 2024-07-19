package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.BuildConfig
import com.team6.connectbca.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.POST

interface LoginService {
//    @FormUrlEncoded
    @POST(BuildConfig.LOGIN_URL)
    suspend fun userLogin(
//        @Field("username") username: String,
//        @Field("password") password: String,
    ) : LoginResponse
}