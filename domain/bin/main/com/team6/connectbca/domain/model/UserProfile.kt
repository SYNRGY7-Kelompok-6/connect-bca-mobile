package com.team6.connectbca.domain.model

data class UserProfile(
    val data: UserProfileData? = null,
    val message: String? = null,
    val status: String? = null
)

data class UserProfileData(
    val address: String? = null,
    val phone: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val birth: String? = null,
    val email: String? = null
)