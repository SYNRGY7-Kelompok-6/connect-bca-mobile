package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile() : UserProfile
    suspend fun updateUserProfile() : UserProfile
}