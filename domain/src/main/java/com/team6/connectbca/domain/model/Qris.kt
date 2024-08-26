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
    val remark: String,
    val amount: QrisAmount? = null,
    val expiresAt: Long? = null
)

data class QrisAmount(
    val value: Double,
    val currency: String
)
data class QrVerifyError(
    val code: String
) {
    fun toEntity(): QrVerifyError {
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
    val qrImage: String,
    val expiresAt: Long? = null
)

data class QrisTransfer(
    val status: String,
    val message: String,
    val data: QrisTransferData? = null
)

data class QrisTransferData(
    val refNumber: String,
    val transactionID: String,
    val amount: TransferAmount,
    val transactionDate: String,
    val remark: String,
    val desc: String,
    val beneficiaryAccountNumber: String,
    val beneficiaryName: String,
    val sourceAccountNumber: String,
    val sourceName: String
)


data class TransferAmount(
    val value: Double,
    val currency: String
)
