package com.team6.connectbca.data.repository

import android.util.Log
import com.team6.connectbca.data.datasource.interfaces.auth.AuthLocalDataSource
import com.team6.connectbca.data.datasource.interfaces.auth.AuthRemoteDataSource
import com.team6.connectbca.data.model.body.LoginBody
import com.team6.connectbca.data.model.body.PinBody
import com.team6.connectbca.data.model.response.LoginResponse
import com.team6.connectbca.data.model.response.LoginResponseData
import com.team6.connectbca.domain.model.Pin
import com.team6.connectbca.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun userLogin(userId: String, password: String) : Boolean {
        val loginBody = LoginBody(userID = userId, password = password)
        var data: LoginResponseData? = null

        try {
            val response: LoginResponse = authRemoteDataSource.userLogin(loginBody)
            data = response.data

            if (data != null) {
                saveSessionData(userId, data.accessToken)
            }
        } catch (error: Throwable) {
            Log.e("Failed with", error.toString())
            error.printStackTrace()
        }

        return data != null
    }

    override suspend fun saveSessionData(userId: String, token: String) {
        authLocalDataSource.saveSessionData(userId, token)
    }

    override suspend fun getSessionData(): Map<String, Any> {
        return authLocalDataSource.getSessionData()
    }

    override suspend fun clearToken() {
        return authLocalDataSource.clearToken()
    }

    override suspend fun clearSessionTime() {
        return authLocalDataSource.clearSessionTime()
    }

    override suspend fun pinValidation(pin: String): Pin {
        val pinBody = PinBody(pin = pin)
        var response = authRemoteDataSource.pinToken(pinBody)
        return response.toEntity()
    }

}