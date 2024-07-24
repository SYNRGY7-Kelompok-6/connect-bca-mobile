package com.team6.connectbca.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class LoginResponse(

	@field:SerializedName("data")
	val data: LoginResponseData? = null

)

