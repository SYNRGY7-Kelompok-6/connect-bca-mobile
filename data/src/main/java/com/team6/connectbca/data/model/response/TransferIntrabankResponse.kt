package com.team6.connectbca.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.Transfer
import com.team6.connectbca.domain.model.TransferAmount
import com.team6.connectbca.domain.model.TransferData

@Keep
data class TransferIntrabankResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: TransferIntrabankDataResponse? = null
) {
    fun toEntity(): Transfer {
        return Transfer(
            status = this.status,
            message = this.message,
            data = this.data?.toEntity()
        )
    }
}

@Keep
data class TransferIntrabankDataResponse(
    @SerializedName("refNumber")
    val refNumber: String,

    @SerializedName("transactionId")
    val transactionId: String,

    @SerializedName("amount")
    val amount: TransferAmountResponse,

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
) {
    fun toEntity() : TransferData{
        return TransferData(
            refNumber = this.refNumber,
            transactionId = this.transactionId,
            amount = this.amount.toEntity(),
            transactionDate = this.transactionDate,
            remark = this.remark,
            desc = this.desc,
            beneficiaryAccountNumber = this.beneficiaryAccountNumber,
            beneficiaryName = this.beneficiaryName,
            sourceAccountNumber = this.sourceAccountNumber,
            sourceName = this.sourceName
        )
    }
}

@Keep
data class TransferAmountResponse(
    @SerializedName("value")
    val value: Double,

    @SerializedName("currency")
    val currency: String
) {
    fun toEntity() : TransferAmount {
        return TransferAmount(
            value = this.value,
            currency = this.currency
        )
    }
}
