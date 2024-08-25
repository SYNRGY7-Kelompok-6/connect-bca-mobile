package com.team6.connectbca.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.QrisTransfer
import com.team6.connectbca.domain.model.QrisTransferData
import com.team6.connectbca.domain.model.TransferAmount

@Keep
data class QrisTransferResponse (
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: QrTransferData? = null
) {
    fun toEntity() : QrisTransfer {
        return if (status == "Success") {
            QrisTransfer(
                status = this.status,
                message = this.message,
                data = QrisTransferData(
                    refNumber = this.data?.refNumber ?: "",
                    transactionID = this.data?.transactionID ?: "",
                    amount = TransferAmount(
                        value = this.data?.amount?.value ?: 0.0,
                        currency = this.data?.amount?.currency ?: ""
                    ),
                    transactionDate = this.data?.transactionDate ?: "",
                    remark = this.data?.remark ?: "",
                    desc = this.data?.desc ?: "",
                    beneficiaryAccountNumber = this.data?.beneficiaryAccountNumber ?: "",
                    beneficiaryName = this.data?.beneficiaryName ?: "",
                    sourceAccountNumber = this.data?.sourceAccountNumber ?: "",
                    sourceName = this.data?.sourceName ?: ""
                )
            )
        } else{
            QrisTransfer(
                status = this.status,
                message = this.message,
                data = null,
            )
        }
    }
}

@Keep
data class QrTransferData (
    @field:SerializedName("refNumber")
    val refNumber: String,
    @field:SerializedName("transactionID")
    val transactionID: String,
    @field:SerializedName("amount")
    val amount: QrisTransferAmountResponse,
    @field:SerializedName("transactionDate")
    val transactionDate: String,
    @field:SerializedName("remark")
    val remark: String,
    @field:SerializedName("desc")
    val desc: String,
    @field:SerializedName("beneficiaryAccountNumber")
    val beneficiaryAccountNumber: String,
    @field:SerializedName("beneficiaryName")
    val beneficiaryName: String,
    @field:SerializedName("sourceAccountNumber")
    val sourceAccountNumber: String,
    @field:SerializedName("sourceName")
    val sourceName: String
){
    fun toEntity(): QrisTransferData{
        return QrisTransferData(
            refNumber = this.refNumber,
            transactionID = this.transactionID,
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
data class QrisTransferAmountResponse (
    val value: Double,
    val currency: String
) {
    fun toEntity() : TransferAmount {
        return TransferAmount(
            value = this.value,
            currency = this.currency
        )
    }
}
