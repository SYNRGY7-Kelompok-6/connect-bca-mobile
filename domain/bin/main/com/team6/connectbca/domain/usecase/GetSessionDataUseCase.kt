package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.repository.AuthRepository

class GetSessionDataUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Map<String, Any> {
        return authRepository.getSessionData()
    }
}