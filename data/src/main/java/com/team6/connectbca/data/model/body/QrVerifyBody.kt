package com.team6.connectbca.data.model.body

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class QrVerifyBody(
    @field:SerializedName("payload")
    val payload: String? = null
)
