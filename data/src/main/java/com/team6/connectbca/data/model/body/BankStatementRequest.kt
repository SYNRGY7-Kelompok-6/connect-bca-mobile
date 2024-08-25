package com.team6.connectbca.data.model.body

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BankStatementRequest(
    @field:SerializedName("fromDate")
    val fromDate: String? = null,

    @field:SerializedName("toDate")
    val toDate: String? = null,

    @field:SerializedName("page")
    val page: String? = null,

    @field:SerializedName("pageSize")
    val pageSize: String? = null
)