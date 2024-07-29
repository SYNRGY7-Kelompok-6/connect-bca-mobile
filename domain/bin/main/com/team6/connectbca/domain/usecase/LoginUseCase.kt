package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        userId: String,
        password: String
    ) : Boolean {
        return authRepository.userLogin(userId, password)
    }
}