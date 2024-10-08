package com.team6.connectbca.data.model.body

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateProfileBody(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("birth")
    val birth: String? = null,

    @field:SerializedName("address")
    val address: String? = null,
)
