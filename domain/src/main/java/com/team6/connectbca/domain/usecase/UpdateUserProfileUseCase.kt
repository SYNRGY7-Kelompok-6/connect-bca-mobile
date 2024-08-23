package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.UserProfileData
import com.team6.connectbca.domain.repository.UserRepository
import java.io.File

class UpdateUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String?,
        email: String?,
        phone: String?,
        birth: String?,
        address: String?,
        image: File?
    ) : UserProfileData? {
        return userRepository.updateUserProfile(name, email, phone, birth, address, image).data
    }
}