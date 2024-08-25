package com.team6.connectbca.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.Pin
import com.team6.connectbca.domain.model.PinData

@Keep
data class PinResponse(
    @field:SerializedName("status")
    var status: String? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("data")
    var data: PinData? = null


){
    fun toEntity() : Pin {
        return Pin(
            status = this.status ?: "",
            message = this.message ?: "",
            data = PinData(
                pinToken = this.data?.pinToken ?: ""
            )
        )
    }
}

@Keep
class PinDataResponse(
    @field:SerializedName("pinToken")
    var pinToken: String? = null,

){
    fun toEntity() : PinData {
        return PinData(
            pinToken = this.pinToken ?: ""
        )
    }
}