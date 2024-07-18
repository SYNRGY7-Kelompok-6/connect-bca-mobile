package com.team6.connectbca.data.model

import androidx.annotation.Keep

@Keep
data class User(
    val id: Int,
    val userId: String,
    val password: String,
    val token: String
)
