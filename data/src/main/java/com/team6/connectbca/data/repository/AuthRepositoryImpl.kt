package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.AuthLocalDataSource
import com.team6.connectbca.data.datasource.interfaces.AuthRemoteDataSource
import com.team6.connectbca.data.model.LoginResponse
import com.team6.connectbca.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun userLogin(userId: String, password: String) : Boolean {
        val response: LoginResponse = authRemoteDataSource.userLogin(userId, password)
        val data = response.data;

        if (data != null) {
            saveSessionData(userId, data.accessToken!!)
        }
        return data == null
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

    private fun checkUserData(userId: String, password: String): Int {
        if (userId.isNullOrEmpty()) {
            return 1
        } else if (password.isNullOrEmpty()) {
            return 2
        }
        return 0
    }
}