package com.team6.connectbca.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginResponseData(

    @field:SerializedName("accessToken") val accessToken: String? = null

)
