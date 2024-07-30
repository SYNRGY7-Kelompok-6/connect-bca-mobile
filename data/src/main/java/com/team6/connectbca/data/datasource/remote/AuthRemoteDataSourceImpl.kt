package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.auth.AuthRemoteDataSource
import com.team6.connectbca.data.datasource.services.LoginService
import com.team6.connectbca.data.model.UserResponse

class AuthRemoteDataSourceImpl(
    private val loginService : LoginService
) : AuthRemoteDataSource {
    override suspend fun userLogin(userId: String, password: String) : UserResponse? {
        return loginService.userLogin(userId, password)
    }
}