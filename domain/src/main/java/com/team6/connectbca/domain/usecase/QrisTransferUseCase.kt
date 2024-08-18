package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.QrisTransfer
import com.team6.connectbca.domain.model.ShowQr
import com.team6.connectbca.domain.model.Transfer
import com.team6.connectbca.domain.repository.QrisRepository
import com.team6.connectbca.domain.repository.TransferRepository
import netscape.javascript.JSObject

class QrisTransferUseCase(private val transferRepository: TransferRepository) {
    suspend operator fun invoke(
        pinToken: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amountValue: Double,
        currency: String
    ): Transfer {

        return transferRepository.transfer(
            pinToken,
            beneficiaryAccountNumber = beneficiaryAccountNumber,
            remark = "Qris Transfer",
            desc = desc,
            amountValue = amountValue,
            currency = currency
        )
    }
}

//class ShowQrUseCase(private val qrisRepository: QrisRepository) {
//    suspend operator fun invoke(option:String): ShowQr {
//        return qrisRepository.showQr(option)
//    }
//
//}