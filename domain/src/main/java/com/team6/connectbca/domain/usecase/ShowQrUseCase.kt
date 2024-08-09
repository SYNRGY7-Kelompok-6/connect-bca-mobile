package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.ShowQr
import com.team6.connectbca.domain.repository.QrisRepository

class ShowQrUseCase(private val qrisRepository: QrisRepository) {
    suspend operator fun invoke(mode:String, option:String): ShowQr {
        return qrisRepository.showQr(mode, option)
    }

}