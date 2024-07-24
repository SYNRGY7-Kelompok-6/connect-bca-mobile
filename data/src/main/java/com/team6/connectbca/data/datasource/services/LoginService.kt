package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.LOGIN_URL
import com.team6.connectbca.data.model.body.LoginBody
import com.team6.connectbca.data.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @POST(LOGIN_URL)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun userLogin(@Body loginBody: LoginBody) : LoginResponse
}