package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.TransactionAmount
import com.team6.connectbca.domain.model.Transfer
import com.team6.connectbca.domain.repository.TransferRepository

class TransferUseCase(private val transferRepository: TransferRepository) {
    suspend fun execute(
        token: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amount: TransactionAmount
    ): TransferInfo {
        if (token.isEmpty() || beneficiaryAccountNumber.isEmpty() || (amount.value ?: 0) <= 0) {
            return TransferInfo.Failure("Invalid input")
        }

        return try {
            val transfer = transferRepository.makeTransfer(token, beneficiaryAccountNumber, remark, desc, amount)
            TransferInfo.Success(transfer)
        } catch (e: Exception) {
            TransferInfo.Failure(e.message ?: "Unknown error")
        }
    }

    sealed class TransferInfo {
        data class Success(val transfer: Transfer) : TransferInfo()
        data class Failure(val errorMessage: String) : TransferInfo()
    }
}