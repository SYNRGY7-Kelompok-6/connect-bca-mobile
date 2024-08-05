package com.team6.connectbca.data.model.body

import com.google.gson.annotations.SerializedName

data class QrVerifyBody(
    @field:SerializedName("payload")
    val payload: String? = null
)
