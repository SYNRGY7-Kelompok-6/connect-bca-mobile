package com.team6.connectbca.domain.usecase

import java.util.logging.Level
import java.util.logging.Logger
import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.domain.repository.BankStatementRepository

class GetBalanceInquiryUseCase(
    private val bankStatementRepository: BankStatementRepository
) {
    private val logger: Logger = Logger.getLogger(GetBalanceInquiryUseCase::class.java.name)

    suspend operator fun invoke(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): AccountInfo {
        return try {
            val response = bankStatementRepository.getBankStatement(token, fromDate, toDate, page, pageSize)
            logger.log(Level.INFO, "Response: $response")
            response.accountInfo ?: throw Exception("Account info is null")
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Error: ${e.message}", e)
            throw e
        }
    }
}