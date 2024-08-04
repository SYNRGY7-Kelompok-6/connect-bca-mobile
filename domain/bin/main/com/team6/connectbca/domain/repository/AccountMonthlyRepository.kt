package com.team6.connectbca.domain.repository

import com.team6.connectbca.domain.model.AccountMonthly

interface AccountMonthlyRepository {
    suspend fun getAccountMonthly(token: String, month: String) : AccountMonthly
}