package com.team6.connectbca.data.model.body

import com.google.gson.annotations.SerializedName
import com.team6.connectbca.data.model.response.Amount

data class TransferIntrabankRequest(
    @SerializedName("beneficiaryAccountNumber")
    val beneficiaryAccountNumber: String,

    @SerializedName("remark")
    val remark: String,

    @SerializedName("desc")
    val desc: String,

    @SerializedName("amount")
    val amount: Amount
)