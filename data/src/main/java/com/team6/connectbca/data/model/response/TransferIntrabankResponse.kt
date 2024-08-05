package com.team6.connectbca.data.model.response

import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.Transfer

data class TransferIntrabankResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: TransferIntrabankData
) {
    fun toEntity() : Transfer {
        return Transfer()
    }
}

data class TransferIntrabankData(
    @SerializedName("refNumber")
    val refNumber: String,

    @SerializedName("transactionId")
    val transactionId: String,

    @SerializedName("amount")
    val amount: Amount,

    @SerializedName("transactionDate")
    val transactionDate: String,

    @SerializedName("remark")
    val remark: String,

    @SerializedName("desc")
    val desc: String,

    @SerializedName("beneficiaryAccountNumber")
    val beneficiaryAccountNumber: String,

    @SerializedName("beneficiaryName")
    val beneficiaryName: String,

    @SerializedName("sourceAccountNumber")
    val sourceAccountNumber: String,

    @SerializedName("sourceName")
    val sourceName: String
)

data class Amount(
    @SerializedName("value")
    val value: Int,

    @SerializedName("currency")
    val currency: String
)