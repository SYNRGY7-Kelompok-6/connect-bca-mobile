package com.team6.connectbca.domain.model

data class User(
    val id: Int,
    val userId: String,
    val password: String,
    val token: String
)
