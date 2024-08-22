package com.team6.connectbca.data.datasource.interfaces.user

import com.team6.connectbca.data.model.body.UpdateProfileBody
import com.team6.connectbca.data.model.response.UserProfileResponse

interface UserRemoteDataSource {
    suspend fun getUserProfile() : UserProfileResponse
    suspend fun updateUserProfile(updateProfileBody: UpdateProfileBody) : UserProfileResponse
}