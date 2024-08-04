package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.QrVerify
import com.team6.connectbca.domain.model.QrVerifyData
import com.team6.connectbca.domain.repository.QrisRepository

class QrVerifyUseCase(private val qrisRepository: QrisRepository) {
    suspend operator fun invoke(token: String,payload: String) : QrVerify {
        return qrisRepository.verifyQr(token, payload)
    }
}
