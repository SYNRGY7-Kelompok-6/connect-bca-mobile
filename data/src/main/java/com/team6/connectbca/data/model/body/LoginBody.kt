package com.team6.connectbca.data.model.body

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginBody(

	@field:SerializedName("userID")
	val userID: String? = null,

	@field:SerializedName("password")
	val password: String? = null

)
