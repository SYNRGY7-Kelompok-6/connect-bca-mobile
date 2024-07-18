package com.team6.connectbca.data.model

import androidx.annotation.Keep

@Keep
data class UserResponse(
    val id: String,
    val token: String,
    val message: String
)
