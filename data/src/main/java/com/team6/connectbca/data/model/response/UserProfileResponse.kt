package com.team6.connectbca.data.model.response

import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.UserProfile
import com.team6.connectbca.domain.model.UserProfileData

data class UserProfileResponse(

    @field:SerializedName("data")
	val data: UserProfileDataResponse? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: String? = null
) {
	fun toEntity() : UserProfile {
		return UserProfile(
			data = this.data?.toEntity(),
			message = this.message,
			status = this.status
		)
	}
}

data class UserProfileDataResponse(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("birth")
	val birth: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) {
	fun toEntity() : UserProfileData {
		return UserProfileData(
			address = this.address,
			phone = this.phone,
			imageUrl = this.imageUrl,
			name = this.name,
			birth = this.birth,
			email = this.email
		)
	}
}
