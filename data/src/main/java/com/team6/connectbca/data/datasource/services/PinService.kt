package com.team6.connectbca.data.datasource.services

import com.team6.connectbca.data.PIN_VALIDATE
import com.team6.connectbca.data.model.body.PinBody
import com.team6.connectbca.data.model.response.PinResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PinService {
    @POST(PIN_VALIDATE)
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    suspend fun pinValidate(@Body pinBody: PinBody): PinResponse
}