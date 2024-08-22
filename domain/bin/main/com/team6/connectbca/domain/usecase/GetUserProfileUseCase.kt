package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.UserProfileData
import com.team6.connectbca.domain.repository.UserRepository

class GetUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() : UserProfileData? {
        return userRepository.getUserProfile().data
    }
}