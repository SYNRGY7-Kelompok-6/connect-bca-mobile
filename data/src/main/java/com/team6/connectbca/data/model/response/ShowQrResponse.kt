package com.team6.connectbca.data.model.response

import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.ShowQr
import com.team6.connectbca.domain.model.ShowQrData

data class ShowQrResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: ShowQrDataResponse? = null,
) {
    fun toEntity(): ShowQr {
        return ShowQr(
            status = this.status,
            message = this.message,
            data = this.data?.toEntity()
        )
    }
}

data class ShowQrDataResponse(
    @field:SerializedName("qrImage")
    val qrImage: String,
    @field:SerializedName("expiresAt")
    val expiresAt: Long? = null,
) {
    fun toEntity(): ShowQrData {
        return ShowQrData(
            qrImage = this.qrImage,
            expiresAt = this.expiresAt
        )
    }
}