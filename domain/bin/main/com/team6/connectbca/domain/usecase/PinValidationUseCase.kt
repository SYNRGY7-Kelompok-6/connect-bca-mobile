package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.Pin
import com.team6.connectbca.domain.repository.AuthRepository

class PinValidationUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(pin: String): Pin {
        return authRepository.pinValidation(pin)
    }
}