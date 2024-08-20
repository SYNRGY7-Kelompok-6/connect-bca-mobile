package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.QrVerify
import com.team6.connectbca.domain.model.QrisTransfer
import com.team6.connectbca.domain.model.ShowQr

interface QrisRepository {
    suspend fun verifyQr(token: String, payload: String): QrVerify

    suspend fun showQr(amountValue: Double, currency: String): ShowQr

    suspend fun qrisTransfer(
        pinToken: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amountValue: Double,
        currency: String
    ): QrisTransfer
}