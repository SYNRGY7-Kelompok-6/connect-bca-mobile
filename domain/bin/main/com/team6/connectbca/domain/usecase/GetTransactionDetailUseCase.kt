package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.TransactionDetailData
import com.team6.connectbca.domain.repository.TransferRepository

class GetTransactionDetailUseCase(
    private val transferRepository: TransferRepository
) {
    suspend operator fun invoke(
        transactionId: String
    ): TransactionDetailData? {
        return transferRepository.getTransactionDetail(transactionId).data
    }
}