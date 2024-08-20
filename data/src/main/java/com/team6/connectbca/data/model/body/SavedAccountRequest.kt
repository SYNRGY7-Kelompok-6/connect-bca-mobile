package com.team6.connectbca.data.model.body

import com.google.gson.annotations.SerializedName

data class SaveAccountRequest(
    @SerializedName("beneficiaryAccountNumber")
    val beneficiaryAccountNumber: String
)