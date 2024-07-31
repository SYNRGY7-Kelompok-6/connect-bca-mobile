package com.team6.connectbca.data.datasource.interfaces.auth

import com.team6.connectbca.data.model.body.LoginBody
import com.team6.connectbca.data.model.response.LoginResponse

interface AuthRemoteDataSource {
    suspend fun userLogin(loginBody: LoginBody) : LoginResponse

}