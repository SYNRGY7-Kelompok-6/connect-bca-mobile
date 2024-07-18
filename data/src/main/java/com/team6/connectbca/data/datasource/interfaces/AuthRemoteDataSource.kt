package com.team6.connectbca.data.datasource.interfaces

import com.team6.connectbca.data.model.UserResponse

interface AuthRemoteDataSource {
    suspend fun userLogin(userId: String, password: String) : UserResponse?

}