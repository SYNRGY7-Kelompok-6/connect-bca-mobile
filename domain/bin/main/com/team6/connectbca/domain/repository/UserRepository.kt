package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.UserProfile
import java.io.File

interface UserRepository {
    suspend fun getUserProfile() : UserProfile
    suspend fun updateUserProfile(
        name: String?,
        email: String?,
        phone: String?,
        birth: String?,
        address: String?,
        image: File?
    ) : UserProfile
}