package com.team6.connectbca.data.datasource.interfaces

interface AuthLocalDataSource {
    suspend fun saveSessionData(userId: String, token: String)
    suspend fun getSessionData() : Map<String, Any>
    suspend fun clearToken()
    suspend fun checkUserId(userId: String) : Boolean
}