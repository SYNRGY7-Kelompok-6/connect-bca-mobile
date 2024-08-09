package com.team6.connectbca.domain.model

data class QrVerify(
    val status: String,
    val message: String,
    val data: QrVerifyData? = null,
    val error: QrVerifyData? = null
)

data class QrVerifyData(
    val beneficiaryAccountNumber: String,
    val beneficiaryName: String,
    val remark: String
)

data class QrVerifyError(
    val code: String
) {
    fun toEntity() : QrVerifyError {
        return QrVerifyError(
            code = this.code
        )
    }
}

data class ShowQr(
    val status: String,
    val message: String,
    val data: ShowQrData? = null,
)

data class ShowQrData(
    val qrImage: String
)