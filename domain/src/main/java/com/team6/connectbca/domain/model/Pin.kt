package com.team6.connectbca.domain.model

data class Pin(
    val status: String,
    val message: String,
    val data: PinData? = null
)

data class PinData (
    val pinToken: String
)
