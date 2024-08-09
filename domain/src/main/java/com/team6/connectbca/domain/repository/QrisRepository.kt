package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.QrVerify
import com.team6.connectbca.domain.model.ShowQr

interface QrisRepository {
    suspend fun verifyQr(token: String, payload: String): QrVerify

    suspend fun showQr(mode:String, option:String): ShowQr
}