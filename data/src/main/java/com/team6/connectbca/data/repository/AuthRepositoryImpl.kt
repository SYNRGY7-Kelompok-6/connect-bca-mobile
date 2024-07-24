package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.AuthLocalDataSource
import com.team6.connectbca.data.datasource.interfaces.AuthRemoteDataSource
import com.team6.connectbca.data.model.UserResponse
import com.team6.connectbca.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authLocalDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun userLogin(userId: String, password: String) : String {
        val isUserDataEmpty = checkUserData(userId, password)

        return if (isUserDataEmpty > 0) {
            val response: UserResponse? = remoteDataSource.userLogin(userId, password)

            saveSessionData(userId, response?.token!!)

            response.message
        } else if (isUserDataEmpty == 1) {
            "User ID tidak boleh kosong."
        } else {
            "Password tidak boleh kosong."
        }
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

    private fun checkUserData(userId: String, password: String): Int {
        if (userId.isNullOrEmpty()) {
            return 1
        } else if (password.isNullOrEmpty()) {
            return 2
        }
        return 0
    }
}