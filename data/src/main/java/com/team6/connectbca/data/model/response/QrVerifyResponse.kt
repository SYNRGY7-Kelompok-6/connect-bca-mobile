package com.team6.connectbca.data.model.response

import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.QrVerify
import com.team6.connectbca.domain.model.QrVerifyData
import com.team6.connectbca.domain.model.QrisAmount

data class QrVerifyResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: QrVerifyDataResponse? = null,

    @field:SerializedName("error")
    val error: QrVerifyDataResponse? = null


) {
    fun toEntity(): QrVerify {
        return QrVerify(
            status = this.status,
            message = this.message,
            data = this.data?.toEntity(),
            error = this.error?.toEntity()
        )
    }
}

data class QrVerifyDataResponse(
    @field:SerializedName("beneficiaryAccountNumber")
    val beneficiaryAccountNumber: String,

    @field:SerializedName("beneficiaryName")
    val beneficiaryName: String,

    @field:SerializedName("amount")
    val amount: QrisAmountResponse? = null, // Opsional

    @field:SerializedName("remark")
    val remark: String,

    @field:SerializedName("expiresAt")
    val expiresAt: Long? = null // Opsional
) {
    fun toEntity(): QrVerifyData {
        return QrVerifyData(
            beneficiaryAccountNumber = this.beneficiaryAccountNumber,
            beneficiaryName = this.beneficiaryName,
            remark = this.remark,
            amount = this.amount?.toEntity(),
            expiresAt = this.expiresAt
        )
    }
}

data class QrisAmountResponse(
    @field:SerializedName("value")
    val value: Double,

    @field:SerializedName("currency")
    val currency: String
) {
    fun toEntity(): QrisAmount {
        return QrisAmount(
            value = this.value,
            currency = this.currency
        )
    }
}