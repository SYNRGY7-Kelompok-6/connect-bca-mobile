package com.team6.connectbca.data.model.response

import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.QrVerify
import com.team6.connectbca.domain.model.QrVerifyData

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
    fun toEntity() : QrVerify {
        return if (status == "Success") {
            QrVerify(
                status = this.status,
                message = this.message,
                data = QrVerifyDataResponse(
                    beneficiaryAccountNumber = this.data?.beneficiaryAccountNumber ?: "",
                    beneficiaryName = this.data?.beneficiaryName ?: "",
                    remark = this.data?.remark ?: ""
                ).toEntity()
            )
        } else {
            QrVerify(
                status = this.status,
                message = this.message,
                error = this.error?.toEntity()
            )
        }
    }
}

data class QrVerifyDataResponse(
    @field:SerializedName("beneficiaryAccountNumber")
    val beneficiaryAccountNumber: String,
    @field:SerializedName("beneficiaryName")
    val beneficiaryName: String,
    @field:SerializedName("remark")
    val remark: String
) {
    fun toEntity() : QrVerifyData {
        return QrVerifyData (
            beneficiaryAccountNumber = this.beneficiaryAccountNumber,
            beneficiaryName = this.beneficiaryName,
            remark = this.remark
        )
    }
}