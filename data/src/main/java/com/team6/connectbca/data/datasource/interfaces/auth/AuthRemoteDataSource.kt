package com.team6.connectbca.data.datasource.interfaces.auth

import com.team6.connectbca.data.model.body.LoginBody
import com.team6.connectbca.data.model.body.PinBody
import com.team6.connectbca.data.model.response.LoginResponse
import com.team6.connectbca.data.model.response.PinResponse

interface AuthRemoteDataSource {
    suspend fun userLogin(loginBody: LoginBody): LoginResponse

    suspend fun pinToken(pinBody: PinBody): PinResponse
}