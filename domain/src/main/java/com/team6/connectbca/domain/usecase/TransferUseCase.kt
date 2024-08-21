package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.TransactionAmount
import com.team6.connectbca.domain.model.Transfer
import com.team6.connectbca.domain.repository.TransferRepository

class TransferUseCase(private val transferRepository: TransferRepository) {
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
            beneficiaryAccountNumber,
            remark,
            desc,
            amountValue,
            currency
        )
    }
}