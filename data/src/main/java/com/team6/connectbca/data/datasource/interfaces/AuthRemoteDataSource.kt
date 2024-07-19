package com.team6.connectbca.data.datasource.interfaces

import com.team6.connectbca.data.model.LoginResponse

interface AuthRemoteDataSource {
    suspend fun userLogin(userId: String, password: String) : LoginResponse

}