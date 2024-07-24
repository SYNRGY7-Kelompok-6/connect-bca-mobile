package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.AuthRemoteDataSource
import com.team6.connectbca.data.datasource.services.LoginService
import com.team6.connectbca.data.model.body.LoginBody
import com.team6.connectbca.data.model.response.LoginResponse

class AuthRemoteDataSourceImpl(
    private val loginService : LoginService
) : AuthRemoteDataSource {
    override suspend fun userLogin(loginBody: LoginBody) : LoginResponse {
        return loginService.userLogin(loginBody)
    }
}