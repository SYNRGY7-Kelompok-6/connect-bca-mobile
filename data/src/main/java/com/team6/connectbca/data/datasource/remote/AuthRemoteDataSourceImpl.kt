package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.AuthRemoteDataSource
import com.team6.connectbca.data.datasource.services.LoginService
import com.team6.connectbca.data.model.LoginResponse
import retrofit2.Call

class AuthRemoteDataSourceImpl(
    private val loginService : LoginService
) : AuthRemoteDataSource {
    override suspend fun userLogin(userId: String, password: String) : LoginResponse {
//        return loginService.userLogin(userId, password)
        return loginService.userLogin()
    }
}