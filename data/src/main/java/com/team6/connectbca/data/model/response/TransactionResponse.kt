package com.team6.connectbca.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.Transaction
import com.team6.connectbca.domain.model.TransactionData

@Keep
data class TransactionResponse(
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("data")
    val data: List<TransactionDataResponse>? = null,

    ) {
    fun toEntity(): Transaction {
        return Transaction(
            message = this.message,
            status = this.status,
            data = this.data?.map { it.toEntity() }
        )
    }
}

@Keep
data class TransactionDataResponse(
    @field:SerializedName("sourceName")
    val sourceName: String? = null,
    @field:SerializedName("amount")
    val amount: Double? = null,
    @field:SerializedName("transactionDate")
    val transactionDate: String? = null,
) {
    fun toEntity(): TransactionData {
        return TransactionData(
            sourceName = this.sourceName,
            amount = this.amount,
            transactionDate = this.transactionDate
        )
    }
}

