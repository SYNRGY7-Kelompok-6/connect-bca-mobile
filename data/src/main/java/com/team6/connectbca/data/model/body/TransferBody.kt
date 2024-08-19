package com.team6.connectbca.data.model.body

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TransferBody(
    @field:SerializedName("beneficiaryAccountNumber")
    val beneficiaryAccountNumber: String,
    @field:SerializedName("remark")
    val remark: String,
    @field:SerializedName("desc")
    val desc: String,
    @field:SerializedName("amount")
    val amount: TransferAmountBody,
)

data class TransferAmountBody(
    @field:SerializedName("value")
    val value: Double,
    @field:SerializedName("currency")
    val currency: String,
)
