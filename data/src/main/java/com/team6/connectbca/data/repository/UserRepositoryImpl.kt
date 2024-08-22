package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.user.UserRemoteDataSource
import com.team6.connectbca.data.model.body.UpdateProfileBody
import com.team6.connectbca.domain.model.UserProfile
import com.team6.connectbca.domain.repository.UserRepository
import java.io.File

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    override suspend fun getUserProfile() : UserProfile {
        return userRemoteDataSource.getUserProfile().toEntity()
    }

    override suspend fun updateUserProfile(
        name: String?,
        email: String?,
        phone: String?,
        birth: String?,
        address: String?,
        image: File?
    ): UserProfile {
        val updateProfileBody = UpdateProfileBody(name, email, phone, birth, address, image)

        return userRemoteDataSource.updateUserProfile(updateProfileBody).toEntity()
    }
}