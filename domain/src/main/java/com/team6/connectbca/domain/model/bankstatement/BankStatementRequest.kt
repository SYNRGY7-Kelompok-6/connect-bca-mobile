package com.team6.connectbca.data.model.bankstatement

import com.google.gson.annotations.SerializedName

data class BankStatementRequest(
    @SerializedName("accountNo") val accountNo: String
)