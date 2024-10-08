package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.Pin

interface AuthRepository {
    suspend fun userLogin(userId: String, password: String): Boolean
    suspend fun saveSessionData(userId: String, token: String)
    suspend fun getSessionData(): Map<String, Any>
    suspend fun clearToken()
    suspend fun clearSessionTime()
    suspend fun pinValidation(pin: String): Pin
}