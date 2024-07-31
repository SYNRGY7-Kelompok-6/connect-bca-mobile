package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.clearToken()
        authRepository.clearSessionTime()
    }
}