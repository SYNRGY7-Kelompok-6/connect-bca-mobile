package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.auth.AuthRemoteDataSource
import com.team6.connectbca.data.datasource.services.LoginService
import com.team6.connectbca.data.datasource.services.PinService
import com.team6.connectbca.data.model.body.LoginBody
import com.team6.connectbca.data.model.body.PinBody
import com.team6.connectbca.data.model.response.LoginResponse
import com.team6.connectbca.data.model.response.PinResponse

class AuthRemoteDataSourceImpl(
    private val loginService: LoginService,
    private val pinService: PinService

) : AuthRemoteDataSource {
    override suspend fun userLogin(loginBody: LoginBody): LoginResponse {
        return loginService.userLogin(loginBody)
    }

    override suspend fun pinToken(pinBody: PinBody): PinResponse {
        return pinService.pinValidate(pinBody)
    }
}